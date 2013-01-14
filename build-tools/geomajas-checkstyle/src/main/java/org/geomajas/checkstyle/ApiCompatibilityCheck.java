/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.checkstyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checkstyle check which verifies Geomajas' API compatibility rules.
 *
 * @author Joachim Van der Auwera
 */
public class ApiCompatibilityCheck extends Check {

	private String packageName;
	private String shortClassName;
	private String fullyQualifiedClassName;
	private Map<String, VersionAndCheck> checkApi = new LinkedHashMap<String, VersionAndCheck>();
	private List<String> api = new ArrayList<String>();
	private boolean isAnnotated;
	private boolean isAllMethods;
	private boolean isInterface;
	private boolean isClass;
	private boolean hasConstructor;
	private String classSince;

	private String basedir;
	private String checkInputFile = "src/main/resources/api.txt";
	private String checkOutputFile = "target/api.txt";
	
	private static final Set<String> neverApiSignatures;

	static {
		neverApiSignatures = new HashSet<String>();
		neverApiSignatures.add("String toString()");
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public void setCheckInputFile(String checkInputFile) {
		this.checkInputFile = checkInputFile;
	}

	public void setCheckOutputFile(String checkOutputFile) {
		this.checkOutputFile = checkOutputFile;
	}

	@Override
	public int[] getDefaultTokens() {
		return getAcceptableTokens();
	}

	@Override
	public int[] getAcceptableTokens() {
		return new int[]{
				TokenTypes.PACKAGE_DEF,
				TokenTypes.CLASS_DEF,
				TokenTypes.INTERFACE_DEF,
				TokenTypes.ANNOTATION_DEF,
				TokenTypes.ENUM_DEF,
				TokenTypes.METHOD_DEF,
				TokenTypes.CTOR_DEF,
				TokenTypes.VARIABLE_DEF,
				TokenTypes.ENUM_CONSTANT_DEF,
		};
	}

	@Override
	public int[] getRequiredTokens() {
		return getAcceptableTokens();
	}

	@Override
	public void beginTree(DetailAST rootAst) {
		super.beginTree(rootAst);
		packageName = "";
		fullyQualifiedClassName = "";
		isAnnotated = false;
		isAllMethods = false;
		classSince = "?";
		isInterface = false;
		isClass = false;
		hasConstructor = false;
	}

	@Override
	public void finishTree(DetailAST rootAst) {
		if (isClass && isAllMethods &&  !hasConstructor) {
			// add default (no-arguments) constructor in api
			int pos =fullyQualifiedClassName.lastIndexOf('.');
			String signature = fullyQualifiedClassName.substring(pos + 1) + "()";
			checkCorrectVersion(rootAst, signature, classSince);
		}
		super.finishTree(rootAst);
	}

	@Override
	public void visitToken(DetailAST ast) {
		switch (ast.getType()) {
			case TokenTypes.PACKAGE_DEF:
				packageName = getPackage(ast);
				break;
			case TokenTypes.CLASS_DEF:
			case TokenTypes.INTERFACE_DEF:
			case TokenTypes.ANNOTATION_DEF:
			case TokenTypes.ENUM_DEF:
				if (null == ast.getParent()) { // do not include inner classes
					shortClassName = getName(ast); 
					fullyQualifiedClassName = packageName + "." + shortClassName;
					checkClassAnnotation(ast);
					isInterface = (TokenTypes.INTERFACE_DEF == ast.getType());
					isClass = (TokenTypes.CLASS_DEF == ast.getType()); 
					if (isAnnotated) {
						String since = getSince(ast);
						api.add(fullyQualifiedClassName + "::" + since);
	
						// @since needs to be specified
						if ("?".equals(since)) {
							log(ast, "classMissingSince", fullyQualifiedClassName);
						}
						// check that class/interface @since has not changed and mark as encountered
						VersionAndCheck vac = checkApi.get(fullyQualifiedClassName + ":");
						if (null != vac) {
							if (!"?".equals(since) && !since.equals(vac.getVersion())) {
								log(ast, "wrongClassSince", vac.getVersion(), since, fullyQualifiedClassName);
							}
							vac.setEncountered(true);
						}
					}
				}
				break;
			case TokenTypes.METHOD_DEF:
			case TokenTypes.CTOR_DEF:
			case TokenTypes.VARIABLE_DEF:    
			case TokenTypes.ENUM_CONSTANT_DEF:
				if (shortClassName.equals(getName(ast.getParent().getParent()))) { // exclude stuff from inner classes
					hasConstructor |= (TokenTypes.CTOR_DEF == ast.getType());
					String signature = getSignature(ast);
					if (isApi(ast) && !neverApiSignatures.contains(signature)) {
						checkCorrectVersion(ast, signature, getSince(ast));
					}
				}
				break;
			default:
				log(ast, "oops, unexpected node");
				break;
		}
	}

	private void checkCorrectVersion(DetailAST ast, String signature, String since) {
		api.add(fullyQualifiedClassName + ":" + signature + ":" + since);

		// check that class/interface @since has not changed and mark as encountered
		VersionAndCheck vac = checkApi.get(fullyQualifiedClassName + ":" + signature);
		if (null != vac) {
			if (!since.equals(vac.getVersion())) {
				log(ast, "wrongMethodSince", vac.getVersion(), since, fullyQualifiedClassName,
						signature);
			}
			vac.setEncountered(true);
		} else {
			// check that version is different from class version (indicates added without @since)
			vac = checkApi.get(fullyQualifiedClassName + ":");
			if (null != vac && since.equals(vac.getVersion())) {
				log(ast, "missingMethodSince", fullyQualifiedClassName, signature);
			}
		}
	}

	private boolean isApi(DetailAST ast) {
		if ("serialVersionUID".equals(getName(ast))) {
			// this should not be considered API
			return false;
		}
		DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
		if (null != modifiers) {
			if (isAllMethods) {
				// if public then it is API
				if (isInterface || null != modifiers.findFirstToken(TokenTypes.LITERAL_PUBLIC)) {
					return true;
				}
			}
			DetailAST check = modifiers.getFirstChild();
			while (null != check) {
				if (TokenTypes.ANNOTATION == check.getType() && "Api".equals(getName(check))) {
					return true;
				}
				check = check.getNextSibling();
			}
		}
		return TokenTypes.ENUM_CONSTANT_DEF == ast.getType();
	}

	private String getSignature(DetailAST ast) {
		String returnType = "";
		String name = getName(ast);
		String parameters = "";
		if (TokenTypes.METHOD_DEF == ast.getType() || TokenTypes.VARIABLE_DEF == ast.getType()) {
			DetailAST modifiersAst = ast.findFirstToken(TokenTypes.MODIFIERS);
			if (null != modifiersAst) {
				if (null != modifiersAst.findFirstToken(TokenTypes.LITERAL_STATIC)) {
					returnType += "static ";
				}
				if (null != modifiersAst.findFirstToken(TokenTypes.FINAL)) {
					returnType += "final ";
				}
			}
			returnType += getTypeAsString(ast.findFirstToken(TokenTypes.TYPE)) + " ";
		}
		if (TokenTypes.METHOD_DEF == ast.getType() || TokenTypes.CTOR_DEF == ast.getType()) {
			DetailAST parametersAst = ast.findFirstToken(TokenTypes.PARAMETERS);
			if (null != parametersAst) {
				DetailAST check = parametersAst.getFirstChild();
				while (null != check) {
					if (TokenTypes.PARAMETER_DEF == check.getType()) {
						parameters += getTypeAsString(check.findFirstToken(TokenTypes.TYPE)) + ", ";
					}
					check = check.getNextSibling();
				}
				parameters = "(" + parameters + ")";
			}
		}
		return returnType + name + parameters;
	}

	private String getTypeAsString(DetailAST typeAst) {
		String type = "";
		if (null != typeAst) {
			DetailAST ast = typeAst.getFirstChild();
			if (TokenTypes.ARRAY_DECLARATOR == ast.getType()) {
				type += getTypeAsString(ast);
				type += "[]";
			} else {
				type += ast.getText();
				if (TokenTypes.IDENT == ast.getType()) {
					ast = ast.getNextSibling();
					if (null != ast && TokenTypes.TYPE_ARGUMENTS == ast.getType()) {
						DetailAST genAst = ast.getFirstChild();
						while (null != genAst) {
							if (TokenTypes.TYPE_ARGUMENT == genAst.getType()) {
								type += getTypeAsString(genAst);
							} else {
								type += genAst.getText();
							}
							genAst = genAst.getNextSibling();
						}
					}
				}
			}
		}
		return type;
	}

	private void checkClassAnnotation(DetailAST ast) {
		DetailAST check = ast.getFirstChild();
		if (TokenTypes.MODIFIERS == check.getType()) {
			check = check.getFirstChild();
			while (null != check) {
				if (TokenTypes.ANNOTATION == check.getType() && "Api".equals(getName(check))) {
					isAnnotated = true;
					classSince = getSince(ast);
					DetailAST param = getToken(TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR, check);
					if (null != param) {
						DetailAST expr = param.getLastChild();
						isAllMethods = "true".equals(expr.getFirstChild().getText());
					}
				}
				check = check.getNextSibling();
			}
		}
	}

	private String getName(DetailAST ast) {
		DetailAST check = ast.getFirstChild();
		String name = null;
		while (null == name && null != check) {
			if (TokenTypes.IDENT == check.getType()) {
				name = check.getText();
			}
			check = check.getNextSibling();
		}
		return name;
	}

	private DetailAST getToken(int type, DetailAST ast) {
		DetailAST check = ast.getFirstChild();
		while (null != check) {
			if (type == check.getType()) {
				return check;
			}
			check = check.getNextSibling();
		}
		return null;
	}

	private String getPackage(DetailAST ast) {
		switch (ast.getType()) {
			case TokenTypes.DOT:
				return getPackage(ast.getFirstChild()) + "." + getPackage(ast.getLastChild());
			case TokenTypes.IDENT:
				return ast.getText();
			case TokenTypes.PACKAGE_DEF:
				DetailAST check = ast.getFirstChild();
				String name = null;
				while (null == name && null != check) {
					name = getPackage(check);
					check = check.getNextSibling();
				}
				return name;
			default:
				return null;
		}
	}

	private String getSince(DetailAST ast) {
		String since = classSince;
		final FileContents contents = getFileContents();
		final TextBlock javadoc = contents.getJavadocBefore(ast.getLineNo());
		if (null != javadoc) {
			for (String line : javadoc.getText()) {
				int index = line.indexOf("@since");
				if (index >= 0) {
					since = line.substring(index + 6).trim();
					int firstSpace = since.indexOf(" ");
					if (firstSpace > 0) {
						since = since.substring(0, firstSpace);
					}
					firstSpace = since.indexOf("\t");
					if (firstSpace > 0) {
						since = since.substring(0, firstSpace);
					}
				}
			}
		} else {
			if (!annotatedWithOverride(ast)) {
				log(ast, "missingJavaDocOnApi", fullyQualifiedClassName, getSignature(ast));
			}
		}
		return since;
	}

	private boolean annotatedWithOverride(DetailAST ast) {
		DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
		if (null != modifiers) {
			DetailAST check = modifiers.getFirstChild();
			String name = getName(check);
			while (null != check) {
				if (TokenTypes.ANNOTATION == check.getType() &&
						("Override".equals(name) || "java.lang.Override".equals(name))) {
					return true;
				}
				check = check.getNextSibling();
			}
		}
		return false;
	}

	@Override
	public void init() {
		try {
			File file = new File(basedir, checkInputFile);
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				String line;
				while (null != (line = reader.readLine())) {
					if (line.length() > 0 && !line.startsWith("//")) {
						int pos = line.lastIndexOf(':');
						checkApi.put(line.substring(0, pos), new VersionAndCheck(line.substring(pos + 1)));
					}
				}
			}
		} catch (IOException ioe) {
			log(0, "Cannot read src/main/resources/api.txt, " + ioe.getMessage());
		}
	}

	@Override
	public void destroy() {
		// output api.txt for comparisons
		Collections.sort(api);
		try {
			File file = new File(basedir, checkOutputFile);
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			for (String line : api) {
				writer.write(line);
				writer.write('\n');
			}
			writer.close();
		} catch (IOException ioe) {
			log(0, "Cannot write target/api.txt, " + ioe.getMessage());
		}

		String problems = "";
		// check that all previous API parts have been encountered
		for (Map.Entry<String, VersionAndCheck> entry : checkApi.entrySet()) {
			if (!entry.getValue().isEncountered()) {
				problems += entry.getKey() + '\n';
			}
		}
		if (problems.length() > 0) {
			throw new RuntimeException("Missing in the API:\n" + problems);
		}
	}

	private class VersionAndCheck {

		private String version;
		private boolean encountered;

		public VersionAndCheck(String version) {
			this.version = version;
		}

		public String getVersion() {
			return version;
		}

		public boolean isEncountered() {
			return encountered;
		}

		public void setEncountered(boolean encountered) {
			this.encountered = encountered;
		}
	}
}
