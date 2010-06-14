/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.checkstyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checkstyle check which verifies Geomajas' API compatibility rules.
 *
 * @author Joachim Van der Auwera
 */
public class ApiCompatibilityCheck extends Check {

	private String packageName;
	private String fullyQualifiedClassName;
	private List<String> api = new ArrayList<String>();
	private boolean isAnnotated;
	private boolean isAllMethods;
	private boolean isInterface;
	private String classSince;

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
				TokenTypes.METHOD_DEF,
				TokenTypes.CTOR_DEF,
				TokenTypes.VARIABLE_DEF,
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
		classSince = "0.0.0";
		isInterface = false;
		System.out.println("------------- Start scanning tree " + rootAst.toString());
	}

	@Override
	public void finishTree(DetailAST rootAst) {
		super.finishTree(rootAst);
		System.out.println("------------- Finish scanning tree " + rootAst.toString());
	}

	@Override
	public void visitToken(DetailAST ast) {
		switch (ast.getType()) {
			case TokenTypes.PACKAGE_DEF:
				packageName = getPackage(ast);
				System.out.println("-----------++ package " + packageName);
				break;
			case TokenTypes.CLASS_DEF:
			case TokenTypes.INTERFACE_DEF:
				fullyQualifiedClassName = packageName + "." + getName(ast);
				System.out.println("-----------++ class/interface " + fullyQualifiedClassName);
				checkClassAnnotation(ast);
				System.out.println("isAnnotated = " + isAnnotated + " allmethods = " + isAllMethods);
				if (isAnnotated) {
					api.add(fullyQualifiedClassName + "::" + getSince(ast));
				}
				if (TokenTypes.INTERFACE_DEF == ast.getType()) {
					isInterface = true;
				}				
				break;
			case TokenTypes.METHOD_DEF:
			case TokenTypes.CTOR_DEF:
			case TokenTypes.VARIABLE_DEF:
				System.out.println("------------- " + getName(ast) + " isApi " + isApi(ast));
				System.out.println("-----------++ " + getSignature(ast) + " since " + getSince(ast));
				if (isApi(ast)) {
					api.add(fullyQualifiedClassName + ":" + getSignature(ast) + ":" + getSince(ast));
				}
				break;
			default:
				log(ast, "oops, unexpected node");
				break;
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
		return false;
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
			DetailAST returnAst = ast.findFirstToken(TokenTypes.TYPE);
			if (null != returnAst) {
				returnType += returnAst.getFirstChild().getText() + " ";
			}
		}
		if (TokenTypes.METHOD_DEF == ast.getType() || TokenTypes.CTOR_DEF == ast.getType()) {
			DetailAST parametersAst = ast.findFirstToken(TokenTypes.PARAMETERS);
			if (null != parametersAst) {
				DetailAST check = parametersAst.getFirstChild();
				while (null != check) {
					if (TokenTypes.PARAMETER_DEF == check.getType()) {
						DetailAST typeAst = ast.findFirstToken(TokenTypes.TYPE);
						if (null != typeAst) {
							parameters += typeAst.getFirstChild().getText() + ", ";
						}
					}
					check = check.getNextSibling();
				}
				parameters = "(" + parameters + ")";
			}
		}
		return returnType + name + parameters;
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
				}
			}
		}
		return since;
	}

	@Override
	public void init() {
		System.out.println("-----------!! init check");
	}

	@Override
	public void destroy() {
		Collections.sort(api);
		try {
			File file = new File("target/api.txt");
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			for (String line : api) {
				writer.write(line);
				writer.write('\n');
			}
			writer.close();
		} catch (IOException ioe) {
			log(0, "Cannot write api.txt, " + ioe.getMessage());
		}
		System.out.println("-----------!! Stop parsing, output \n" + api);
	}
}
