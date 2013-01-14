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

/**
 * Checkstyle TreeWalker replacement which never tries to cache checked files.
 *
 * @author Joachim Van der Auwera
 */

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRecognitionException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.puppycrawl.tools.checkstyle.DefaultContext;
import com.puppycrawl.tools.checkstyle.Defn;
import com.puppycrawl.tools.checkstyle.ModuleFactory;
import com.puppycrawl.tools.checkstyle.api.AbstractFileSetCheck;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.Utils;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedJavaLexer;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedJavaRecognizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Responsible for walking an abstract syntax tree and notifying interested
 * checks at each each node.
 * Modified by Joachim Van der Auwera to always walk all files, no caching.
 *
 * @author Oliver Burn
 * @version 1.0
 *          <p/>
 *          This file was copied from checkstyle and is originally licensed as LGPL.
 */
public final class TreeWalker extends AbstractFileSetCheck {

	/** default distance between tab stops */
	private static final int DEFAULT_TAB_WIDTH = 8;

	/** maps from token name to checks */
	private final Multimap<String, Check> mTokenToChecks = new HashMultimap<String, Check>();
	/** all the registered checks */
	private final Set<Check> mAllChecks = Sets.newHashSet();
	/** the distance between tab stops */
	private int mTabWidth = DEFAULT_TAB_WIDTH;

	/** class loader to resolve classes with. * */
	private ClassLoader mClassLoader;

	/** context of child components */
	private Context mChildContext;

	/** a factory for creating submodules (i.e. the Checks) */
	private ModuleFactory mModuleFactory;

	/**
	 * controls whether we should use recursive or iterative
	 * algorithm for tree processing.
	 */
	private final boolean mRecursive;

	/** logger for debug purpose */
	private static final Log LOG =
			LogFactory.getLog("com.puppycrawl.tools.checkstyle.TreeWalker");

	/** Creates a new <code>TreeWalker</code> instance. */
	public TreeWalker() {
		setFileExtensions(new String[] {"java"});
		// Tree walker can use two possible algorithms for
		// tree processing (iterative and recursive.
		// Recursive is default for now.
		final String recursive =
				System.getProperty("checkstyle.use.recursive.algorithm", "false");
		mRecursive = "true".equals(recursive);
		if (mRecursive) {
			LOG.debug("TreeWalker uses recursive algorithm");
		} else {
			LOG.debug("TreeWalker uses iterative algorithm");
		}
	}

	/** @param aTabWidth the distance between tab stops */
	public void setTabWidth(int aTabWidth) {
		mTabWidth = aTabWidth;
	}

	public void setCacheFile(String aFileName) {
		// ignore, just for api compatibility
	}

	/** @param aClassLoader class loader to resolve classes with. */
	public void setClassLoader(ClassLoader aClassLoader) {
		mClassLoader = aClassLoader;
	}

	/**
	 * Sets the module factory for creating child modules (Checks).
	 *
	 * @param aModuleFactory the factory
	 */
	public void setModuleFactory(ModuleFactory aModuleFactory) {
		mModuleFactory = aModuleFactory;
	}

	@Override
	public void finishLocalSetup() {
		final DefaultContext checkContext = new DefaultContext();
		checkContext.add("classLoader", mClassLoader);
		checkContext.add("messages", getMessageCollector());
		checkContext.add("severity", getSeverity());
		// TODO: hmmm.. this looks less than elegant
		// we have just parsed the string,
		// now we're recreating it only to parse it again a few moments later
		checkContext.add("tabWidth", String.valueOf(mTabWidth));

		mChildContext = checkContext;
	}

	@Override
	public void setupChild(Configuration aChildConf)
			throws CheckstyleException {
		// TODO: improve the error handing
		final String name = aChildConf.getName();
		final Object module = mModuleFactory.createModule(name);
		if (!(module instanceof Check)) {
			throw new CheckstyleException(
					"TreeWalker is not allowed as a parent of " + name);
		}
		final Check c = (Check) module;
		c.contextualize(mChildContext);
		c.configure(aChildConf);
		c.init();

		registerCheck(c);
	}

	@Override
	protected void processFiltered(File aFile, List<String> aLines) {
		// check if already checked and passed the file
		final String fileName = aFile.getPath();

		try {
			final FileContents contents = new FileContents(fileName, aLines
					.toArray(new String[aLines.size()]));
			final DetailAST rootAST = TreeWalker.parse(contents);
			walk(rootAST, contents);
		} catch (final RecognitionException re) {
			Utils.getExceptionLogger()
					.debug("RecognitionException occured.", re);
			getMessageCollector().add(
					new LocalizedMessage(
							re.getLine(),
							re.getColumn(),
							Defn.CHECKSTYLE_BUNDLE,
							"general.exception",
							new String[] {re.getMessage()},
							getId(),
							this.getClass(), null));
		} catch (final TokenStreamRecognitionException tre) {
			Utils.getExceptionLogger()
					.debug("TokenStreamRecognitionException occured.", tre);
			final RecognitionException re = tre.recog;
			if (re != null) {
				getMessageCollector().add(
						new LocalizedMessage(
								re.getLine(),
								re.getColumn(),
								Defn.CHECKSTYLE_BUNDLE,
								"general.exception",
								new String[] {re.getMessage()},
								getId(),
								this.getClass(), null));
			} else {
				getMessageCollector().add(
						new LocalizedMessage(
								0,
								Defn.CHECKSTYLE_BUNDLE,
								"general.exception",
								new String[]
										{"TokenStreamRecognitionException occured."},
								getId(),
								this.getClass(), null));
			}
		} catch (final TokenStreamException te) {
			Utils.getExceptionLogger()
					.debug("TokenStreamException occured.", te);
			getMessageCollector().add(
					new LocalizedMessage(
							0,
							Defn.CHECKSTYLE_BUNDLE,
							"general.exception",
							new String[] {te.getMessage()},
							getId(),
							this.getClass(), null));
		} catch (final Throwable err) {
			Utils.getExceptionLogger().debug("Throwable occured.", err);
			getMessageCollector().add(
					new LocalizedMessage(
							0,
							Defn.CHECKSTYLE_BUNDLE,
							"general.exception",
							new String[] {"" + err},
							getId(),
							this.getClass(), null));
		}
	}

	/**
	 * Register a check for a given configuration.
	 *
	 * @param aCheck the check to register
	 * @throws CheckstyleException if an error occurs
	 */
	private void registerCheck(Check aCheck)
			throws CheckstyleException {
		final int[] tokens;
		final Set<String> checkTokens = aCheck.getTokenNames();
		if (!checkTokens.isEmpty()) {
			tokens = aCheck.getRequiredTokens();

			//register configured tokens
			final int[] acceptableTokens = aCheck.getAcceptableTokens();
			Arrays.sort(acceptableTokens);
			for (String token : checkTokens) {
				try {
					final int tokenId = TokenTypes.getTokenId(token);
					if (Arrays.binarySearch(acceptableTokens, tokenId) >= 0) {
						registerCheck(token, aCheck);
					}
					// TODO: else log warning
				} catch (final IllegalArgumentException ex) {
					throw new CheckstyleException("illegal token \""
							+ token + "\" in check " + aCheck, ex);
				}
			}
		} else {
			tokens = aCheck.getDefaultTokens();
		}
		for (int element : tokens) {
			registerCheck(element, aCheck);
		}
		mAllChecks.add(aCheck);
	}

	/**
	 * Register a check for a specified token id.
	 *
	 * @param aTokenID the id of the token
	 * @param aCheck the check to register
	 */
	private void registerCheck(int aTokenID, Check aCheck) {
		registerCheck(TokenTypes.getTokenName(aTokenID), aCheck);
	}

	/**
	 * Register a check for a specified token name
	 *
	 * @param aToken the name of the token
	 * @param aCheck the check to register
	 */
	private void registerCheck(String aToken, Check aCheck) {
		mTokenToChecks.put(aToken, aCheck);
	}

	/**
	 * Initiates the walk of an AST.
	 *
	 * @param aAST the root AST
	 * @param aContents the contents of the file the AST was generated from
	 */
	private void walk(DetailAST aAST, FileContents aContents) {
		getMessageCollector().reset();
		notifyBegin(aAST, aContents);

		// empty files are not flagged by javac, will yield aAST == null
		if (aAST != null) {
			if (useRecursiveAlgorithm()) {
				processRec(aAST);
			} else {
				processIter(aAST);
			}
		}

		notifyEnd(aAST);
	}

	/**
	 * Notify interested checks that about to begin walking a tree.
	 *
	 * @param aRootAST the root of the tree
	 * @param aContents the contents of the file the AST was generated from
	 */
	private void notifyBegin(DetailAST aRootAST, FileContents aContents) {
		for (Check ch : mAllChecks) {
			ch.setFileContents(aContents);
			ch.beginTree(aRootAST);
		}
	}

	/**
	 * Notify checks that finished walking a tree.
	 *
	 * @param aRootAST the root of the tree
	 */
	private void notifyEnd(DetailAST aRootAST) {
		for (Check ch : mAllChecks) {
			ch.finishTree(aRootAST);
		}
	}

	/**
	 * Recursively processes a node calling interested checks at each node.
	 * Uses recursive algorithm.
	 *
	 * @param aAST the node to start from
	 */
	private void processRec(DetailAST aAST) {
		if (aAST == null) {
			return;
		}

		notifyVisit(aAST);

		final DetailAST child = aAST.getFirstChild();
		if (child != null) {
			processRec(child);
		}

		notifyLeave(aAST);

		final DetailAST sibling = aAST.getNextSibling();
		if (sibling != null) {
			processRec(sibling);
		}
	}

	/**
	 * Notify interested checks that visiting a node.
	 *
	 * @param aAST the node to notify for
	 */
	private void notifyVisit(DetailAST aAST) {
		final Collection<Check> visitors =
				mTokenToChecks.get(TokenTypes.getTokenName(aAST.getType()));
		for (Check c : visitors) {
			c.visitToken(aAST);
		}
	}

	/**
	 * Notify interested checks that leaving a node.
	 *
	 * @param aAST the node to notify for
	 */
	private void notifyLeave(DetailAST aAST) {
		final Collection<Check> visitors =
				mTokenToChecks.get(TokenTypes.getTokenName(aAST.getType()));
		for (Check ch : visitors) {
			ch.leaveToken(aAST);
		}
	}

	/**
	 * Static helper method to parses a Java source file.
	 *
	 * @param aContents contains the contents of the file
	 * @return the root of the AST
	 * @throws TokenStreamException if lexing failed
	 * @throws RecognitionException if parsing failed
	 */
	public static DetailAST parse(FileContents aContents)
			throws RecognitionException, TokenStreamException {
		final Reader sar = new StringArrayReader(aContents.getLines());
		final GeneratedJavaLexer lexer = new GeneratedJavaLexer(sar);
		lexer.setFilename(aContents.getFilename());
		lexer.setCommentListener(aContents);
		lexer.setTreatAssertAsKeyword(true);
		lexer.setTreatEnumAsKeyword(true);

		final GeneratedJavaRecognizer parser =
				new GeneratedJavaRecognizer(lexer);
		parser.setFilename(aContents.getFilename());
		parser.setASTNodeClass(DetailAST.class.getName());
		parser.compilationUnit();

		return (DetailAST) parser.getAST();
	}

	@Override
	public void destroy() {
		for (Check c : mAllChecks) {
			c.destroy();
		}
		super.destroy();
	}

	/**
	 * @return true if we should use recursive algorithm
	 *         for tree processing, false for iterative one.
	 */
	private boolean useRecursiveAlgorithm() {
		return mRecursive;
	}

	/**
	 * Processes a node calling interested checks at each node.
	 * Uses iterative algorithm.
	 *
	 * @param aRoot the root of tree for process
	 */
	private void processIter(DetailAST aRoot) {
		DetailAST curNode = aRoot;
		while (curNode != null) {
			notifyVisit(curNode);
			DetailAST toVisit = curNode.getFirstChild();
			while ((curNode != null) && (toVisit == null)) {
				notifyLeave(curNode);
				toVisit = curNode.getNextSibling();
				if (toVisit == null) {
					curNode = curNode.getParent();
				}
			}
			curNode = toVisit;
		}
	}
}