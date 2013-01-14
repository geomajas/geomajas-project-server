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

package org.geomajas.widget.utility.gwt.client.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.geomajas.widget.utility.gwt.client.i18n.GuwMessages;

/**
 * <p>
 * Wizard class that lets a user step through a set of pages to accomplish a certain task. In addition to the classical
 * NEXT, BACK, CANCEL and FINISH buttons, this wizard provides direct page navigation by means of a button per page
 * (typically shown in a vertical navigation bar). This abstract class handles all the navigation logic and state
 * handling of the buttons. The NEXT and BACK button are handled directly by this class. The CANCEL and FINISH button
 * should be handled by concrete subclasses by implementing the abstract {@link #onCancel()} and {@link #onFinish()}
 * methods.
 * </p>
 * 
 * <p>
 * The view is abstracted through the {@link WizardView} interface. A default implementation, called
 * {@link WizardWidget} is provided.
 * </p>
 * 
 * <p>
 * Individual pages can be created by sub-classing the {@link WizardPage} class. This class has a template parameter
 * DATA, which represents the type of the data context that is passed to all pages on startup.
 * </p>
 * 
 * <p>
 * Typical usage is as follows:
 * </p>
 * <code>
 * <pre>
 *   Wizard<MyData> wizard = new MyWizard(new WizardWidget<MyData>());
 *   wizard.addPage(new MyPage1());
 *   wizard.addPage(new MyPage2());
 *   wizard.addPage(new MyPage3());
 *   // add the view to your application
 *   layout.addMember(wizard.getView());
 *   // pass the data context and show the first page
 *   wizard.start(new MyData());
 * 
 * 	</pre>
 * </code>
 * 
 * @param <DATA> data type
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class Wizard<DATA> {

	private static final GuwMessages MESSAGES = GWT.create(GuwMessages.class);

	private List<WizardPage<DATA>> pages;

	private WizardPage<DATA> currentPage;

	private WizardView<DATA> wizardView;

	private boolean started;

	/**
	 * Create a wizard with the specified view.
	 * 
	 * @param wizardView
	 *            the view part
	 */
	public Wizard(WizardView<DATA> wizardView) {
		this.wizardView = wizardView;
		pages = new ArrayList<WizardPage<DATA>>();
	}

	/**
	 * Returns the view part of this wizard.
	 * 
	 * @return the view part.
	 */
	public WizardView<DATA> getView() {
		return wizardView;
	}

	/**
	 * Starts or restarts this wizard by clearing all pages and presenting the first page to the user. This method
	 * should only be called after all pages have been added. After this method has been called, no more pages can be
	 * added.
	 * 
	 * @param wizardData initial data
	 */
	public void start(DATA wizardData) {
		if (!started) {
			initHandlers();
		}
		for (WizardPage<DATA> page : pages) {
			page.clear();
			page.setWizardData(wizardData);
		}
		ListIterator<WizardPage<DATA>> iterator = pages.listIterator();
		if (iterator.hasNext()) {
			setCurrentPage(iterator.next());
		}
	}

	/**
	 * Returns the currently visible page.
	 * 
	 * @return the current page
	 */
	public WizardPage<DATA> getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page of this wizard. As this method forces the current page, it should only be used after
	 * checking validity of the previous pages.
	 * 
	 * @param newPage
	 *            the page to be set
	 */
	protected void setCurrentPage(WizardPage<DATA> newPage) {
		currentPage = newPage;
		updateState();
		wizardView.setCurrentPage(currentPage);
		currentPage.show();
	}

	/**
	 * Adds a page to this wizard. The page order is determined by the order in which pages are added.
	 * 
	 * @param page
	 *            the page to be added
	 */
	public void addPage(WizardPage<DATA> page) {
		if (!started) {
			WizardPage<DATA> lastPage = pages.size() > 0 ? pages.get(pages.size() - 1) : null;
			if (lastPage != null) {
				lastPage.setNextPage(page);
				page.setPreviousPage(lastPage);
			}
			pages.add(page);
			wizardView.addPageToView(page);
		}
	}

	/**
	 * Called when the user presses the CANCEL button.
	 */
	public abstract void onCancel();

	/**
	 * Called when the user presses the FINISH button.
	 */
	public abstract void onFinish();

	private void initHandlers() {
		for (WizardButton<DATA> button : wizardView.getButtons()) {
			switch (button.getType()) {
				case PREVIOUS:
					button.addClickHandler(new BackHandler());
					break;
				case CANCEL:
					button.addClickHandler(new CancelHandler());
					break;
				case FINISH:
					button.addClickHandler(new FinishHandler());
					break;
				case NEXT:
					button.addClickHandler(new NextHandler());
					break;
				case PAGE:
					button.addClickHandler(new GoToHandler(button.getPage()));
					break;
				default:
					throw new IllegalStateException("Unknown button type " + button.getType());
			}
		}
		started = true;
	}

	private void updateState() {
		for (WizardButton<DATA> button : wizardView.getButtons()) {
			switch (button.getType()) {
				case PREVIOUS:
					if (currentPage.getPreviousPage() != null) {
						button.setEnabled(true);
					} else {
						button.setEnabled(false);
					}
					break;
				case CANCEL:
					button.setEnabled(true);
					break;
				case FINISH:
					if (currentPage.getNextPage() == null) {
						button.setEnabled(true);
					} else {
						button.setEnabled(false);
					}
					break;
				case NEXT:
					if (currentPage.getNextPage() != null) {
						button.setEnabled(true);
					} else {
						button.setEnabled(false);
					}
					break;
				case PAGE:
					button.setActive(button.getPage() == currentPage);
					button.setEnabled(button.getPage().canShow());
					Widget widget = button.getPage().asWidget();
					if (widget != null) {
						widget.setVisible(button.getPage() == currentPage);
					}
					break;
				default:
					throw new IllegalStateException("Unknown button type " + button.getType());
			}
		}
	}

	/**
	 * Performs FINISH operation.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class FinishHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			onFinish();
		}

	}

	/**
	 * Performs CANCEL operation.
	 * 
	 * @author Jan De Moerloose
	 */
	class CancelHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			onCancel();
		}

	}

	/**
	 * Performs BACK operation.
	 * 
	 * @author Jan De Moerloose
	 */
	class BackHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			setCurrentPage(currentPage.getPreviousPage());
		}

	}

	/**
	 * Performs NEXT operation.
	 * 
	 * @author Jan De Moerloose
	 */
	class NextHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			if (currentPage.validate()) {
				currentPage.savePage(wizardView, new Runnable() {
					public void run() {
						setCurrentPage(currentPage.getNextPage());
					}
				}, new Runnable() {
					public void run() {
						SC.warn(MESSAGES.wizardSavePageFailed());
					}
				});
			}
		}

	}

	/**
	 * Performs page operation.
	 * 
	 * @author Jan De Moerloose
	 */
	class GoToHandler implements ClickHandler {

		private WizardPage<DATA> page;

		public GoToHandler(WizardPage<DATA> page) {
			this.page = page;
		}

		public void onClick(ClickEvent event) {
			// must check all previous pages and stop if we reach an invalid page
			// because user may have changed page info behind our back !
			Iterator<WizardPage<DATA>> it = pages.iterator();
			while (it.hasNext()) {
				WizardPage<DATA> p = it.next();
				if (p == page || !p.validate()) {
					setCurrentPage(p);
					break;
				}
			}
		}

	}

}
