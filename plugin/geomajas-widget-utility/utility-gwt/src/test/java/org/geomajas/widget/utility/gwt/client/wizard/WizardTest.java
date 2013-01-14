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
import java.util.List;

import com.google.gwt.junit.GWTMockUtilities;
import org.geomajas.widget.utility.gwt.client.wizard.WizardButton.ButtonType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Test for {@link Wizard}.
 *
 * @author jan De Moerloose
 */
public class WizardTest {

	@Before
	public void disableWidgets() {
		GWTMockUtilities.disarm();
	}

	@After
	public void reEnableWidgets() {
		GWTMockUtilities.restore();
	}

	@Test
	public void testStartToFinish() {
		TestView view = new TestView();
		TestWizard wizard = new TestWizard(view);
		TestPage page1 = new TestPage("1");
		TestPage page2 = new TestPage("2");
		TestPage page3 = new TestPage("3");
		wizard.addPage(page1);
		wizard.addPage(page2);
		wizard.addPage(page3);
		wizard.start("test");

		// check page and button states
		Assert.assertSame(page1, wizard.getCurrentPage());
		Assert.assertFalse(view.getBack().isEnabled());
		Assert.assertTrue(view.getNext().isEnabled());
		Assert.assertTrue(view.getCancel().isEnabled());
		Assert.assertFalse(view.getFinish().isEnabled());

		Assert.assertTrue(page1.canShow());
		Assert.assertFalse(page2.canShow());
		Assert.assertFalse(page3.canShow());

		// click on next
		view.getNext().fireEvent(new ClickEvent(null));
		// page not validating, button states remain
		Assert.assertFalse(page1.isValid());
		Assert.assertSame(page1, wizard.getCurrentPage());
		Assert.assertFalse(view.getBack().isEnabled());
		Assert.assertTrue(view.getNext().isEnabled());
		Assert.assertTrue(view.getCancel().isEnabled());
		Assert.assertFalse(view.getFinish().isEnabled());

		Assert.assertTrue(page1.canShow());
		Assert.assertFalse(page2.canShow());
		Assert.assertFalse(page3.canShow());

		// make page1 validate
		page1.setValidate(true);
		// click on next
		view.getNext().fireEvent(new ClickEvent(null));
		Assert.assertSame(page2, wizard.getCurrentPage());
		Assert.assertTrue(view.getBack().isEnabled());
		Assert.assertTrue(view.getNext().isEnabled());
		Assert.assertTrue(view.getCancel().isEnabled());
		Assert.assertFalse(view.getFinish().isEnabled());

		Assert.assertTrue(page1.canShow());
		Assert.assertTrue(page2.canShow());
		Assert.assertFalse(page3.canShow());

		// make page2 validate
		page2.setValidate(true);
		view.getNext().fireEvent(new ClickEvent(null));
		Assert.assertSame(page3, wizard.getCurrentPage());
		Assert.assertTrue(view.getBack().isEnabled());
		Assert.assertFalse(view.getNext().isEnabled());
		Assert.assertTrue(view.getCancel().isEnabled());
		Assert.assertTrue(view.getFinish().isEnabled());

		Assert.assertTrue(page1.canShow());
		Assert.assertTrue(page2.canShow());
		Assert.assertTrue(page3.canShow());

	}

	class TestView implements WizardView<String> {

		private TestButton back = new TestButton(ButtonType.PREVIOUS);

		private TestButton next = new TestButton(ButtonType.NEXT);

		private TestButton cancel = new TestButton(ButtonType.CANCEL);

		private TestButton finish = new TestButton(ButtonType.FINISH);

		private List<WizardButton<String>> buttons = new ArrayList<WizardButton<String>>();

		public TestView() {
			buttons.add(back);
			buttons.add(next);
			buttons.add(cancel);
			buttons.add(finish);
		}

		public void addPageToView(WizardPage<String> page) {
			TestButton button = new TestButton(page);
			buttons.add(button);
		}

		public void setCurrentPage(WizardPage<String> currentPage) {
			// not needed here
		}

		public List<WizardButton<String>> getButtons() {
			return buttons;
		}

		public TestButton getBack() {
			return back;
		}

		public TestButton getNext() {
			return next;
		}

		public TestButton getCancel() {
			return cancel;
		}

		public TestButton getFinish() {
			return finish;
		}

		public Widget asWidget() {
			return null;
		}

		public void setLoading(boolean loading) {
		}

	}

	class TestPage extends WizardPage<String> {

		private String id;

		private boolean validate = false;

		public TestPage(String id) {
			this.id = id;
		}

		@Override
		public String getTitle() {
			return id;
		}

		@Override
		public String getExplanation() {
			return id;
		}

		@Override
		public boolean doValidate() {
			return validate;
		}

		public void setValidate(boolean validate) {
			this.validate = validate;
		}

		@Override
		public Canvas asWidget() {
			return null;
		}

		@Override
		public void clear() {
		}

		@Override
		public void show() {

		}

	}

	class TestWizard extends Wizard<String> {

		public TestWizard(WizardView<String> wizardView) {
			super(wizardView);
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onFinish() {
		}

	}

	public class TestButton implements WizardButton<String> {

		private HandlerManager manager = new HandlerManager(this);

		private boolean enabled;

		private ButtonType type;

		private WizardPage<String> page;

		public TestButton(ButtonType type) {
			this.type = type;
		}

		public TestButton(WizardPage<String> page) {
			this.type = ButtonType.PAGE;
			this.page = page;
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return manager.addHandler(ClickEvent.getType(), handler);
		}

		public void fireEvent(GwtEvent<?> event) {
			manager.fireEvent(event);
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public ButtonType getType() {
			return type;
		}

		public void setActive(boolean active) {
		}

		public WizardPage<String> getPage() {
			return page;
		}

	}

}
