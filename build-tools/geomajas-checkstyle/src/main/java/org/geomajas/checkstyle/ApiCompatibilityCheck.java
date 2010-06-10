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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Checkstyle check which verifies Geomajas' API compatibility rules.
 *
 * @author Joachim Van der Auwera
 */
public class ApiCompatibilityCheck extends Check {

	private String packageName;
	private String fullyQualifiedClassName;
	private Map<String, String> api = new LinkedHashMap<String, String>();
	private boolean isAnnotated;
	private boolean isAllMethods;

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
				System.out.println("isAnnotated = " +isAnnotated + " allmethods = " + isAllMethods);
				break;
			case TokenTypes.METHOD_DEF:
			case TokenTypes.CTOR_DEF:
			case TokenTypes.VARIABLE_DEF:
				System.out.println("-----------++ " + getName(ast) + " since " + getSince(ast));
				break;
			default:
				log(ast, "oops, unexpected node");
			    break;
		}
	}

	private void checkClassAnnotation(DetailAST ast) {
		DetailAST check = ast.getFirstChild();
		if (TokenTypes.MODIFIERS == check.getType()) {
			check = check.getFirstChild();
			while (null != check) {
				if (TokenTypes.ANNOTATION == check.getType() && "Api".equals(getName(check))) {
					isAnnotated = true;
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
		String since = "0.0.0";
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
		System.out.println("-----------!! Stop parsing, output \n" + api);
	}
}
