/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main.menu;

import java.awt.HeadlessException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.martus.swing.UiMenu;
import org.miradi.actions.*;
import org.miradi.actions.jump.ActionJumpCloseTheLoop;
import org.miradi.main.EAM;
import org.miradi.main.MiradiMenuItem;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;
import org.miradi.main.ViewSwitcher;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceItemWithChildren;
import org.miradi.questions.DynamicChoiceWithRootChoiceItem;
import org.miradi.questions.OpenStandardsAnalyzeUseAndAdaptQuestion;
import org.miradi.questions.OpenStandardsCaptureAndShareLearningQuestion;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsImplementActionsAndMonitoringQuestion;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;
import org.miradi.utils.MenuItemWithoutLocation;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.targetviability.TargetViabilityView;
import org.miradi.views.threatmatrix.ThreatMatrixView;
import org.miradi.views.umbrella.HelpButtonData;
import org.miradi.views.umbrella.ViewSpecificHelpButtonData;
import org.miradi.views.workplan.WorkPlanView;

public class MainMenuBar extends JMenuBar
{
	
	public MainMenuBar(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}

	public MainMenuBar(Actions actions) throws HeadlessException
	{
		createMenus(actions);
	}
	
	public void updateMenuOptions()
	{
		createMenus(mainWindow.getActions());
	}

	private void createMenus(Actions actions)
	{
		removeAll();
		add(createFileMenu(actions));
		add(createEditMenu(actions));
		JMenu actionsMenu = createActionsMenu(actions);
		if(!isWelcomeView())
			add(createViewMenu(actions));

		if(actionsMenu != null)
			add(actionsMenu);
		
		if(!isWelcomeView())
			add(createProcessMenu(actions));
		add(createHelpMenu(actions));
	}

	private JMenu createFileMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.setMnemonic(KeyEvent.VK_F);

		addMenuItem(actions, menu, ActionSaveProjectAs.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionHowToSave.class, KeyEvent.VK_H);
		addMenuItem(actions, menu, ActionClose.class, KeyEvent.VK_C);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionPrint.class, KeyEvent.VK_P);
                
		menu.addSeparator();
                /*Added menu items for uploading and downloading pojects
		addMenuitem(actions, ActionUploadProject.class, KeyEvent.VK_F1);
                addMenuitem(actions, ActionDownloadProject.class, KeyEvent.VK_F2);
                
                menu.addSeparator();
                */
		menu.add(createExportCurrentPageMenu(actions));
		menu.add(createExportMenu(actions));
		menu.add(createImportMenu(actions));
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionExit.class, KeyEvent.VK_X);
		
		return menu;
	}

	private JMenu createExportMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("Menu|Export Current Project as..."));
		menu.setMnemonic(KeyEvent.VK_R);
		
		addMenuItem(actions, menu, ActionExportZippedProjectFile.class, KeyEvent.VK_M);
		addMenuItem(actions, menu, ActionExportConProXml.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionExportProjectXml.class, KeyEvent.VK_X);
		addMenuItem(actions, menu, ActionExportWcsProjectZip.class);
		
		if(Miradi.isDemoMode())
		{
			menu.addSeparator();
			
			JMenuItem item = addMenuItem(actions, menu, ActionDatabasesDemo.class, KeyEvent.VK_D);
			item.putClientProperty(HelpButtonData.class, new HelpButtonData(HelpButtonData.DEMO, HelpButtonData.IMPORT_AND_EXPORT_HTML));
		}
		
		return menu;
	}
	
	private JMenu createExportCurrentPageMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("Menu|Export Current Page as..."));
		menu.setMnemonic(KeyEvent.VK_E);
		
		addMenuItem(actions, menu, ActionSaveImageJPEG.class, KeyEvent.VK_J);
		addMenuItem(actions, menu, ActionSaveImagePng.class, KeyEvent.VK_P);
		addMenuItem(actions, menu, ActionExportRtf.class, KeyEvent.VK_R);
		addMenuItem(actions, menu, ActionExportTable.class, KeyEvent.VK_T);
		
		return menu;
	}
	
	private JMenu createImportMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("Menu|Import"));
		menu.setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(actions, menu, ActionImportZippedProjectFile.class, KeyEvent.VK_M);
		addMenuItem(actions, menu, ActionImportZippedConproProject.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionImportXmpz.class, KeyEvent.VK_X);
		
		if(Miradi.isDemoMode())
		{
			JMenuItem item = addMenuItem(actions, menu, ActionDatabasesDemo.class, KeyEvent.VK_D);
			item.putClientProperty(HelpButtonData.class, new HelpButtonData(HelpButtonData.DEMO, HelpButtonData.IMPORT_AND_EXPORT_HTML));
		}

		return menu;
	}	
	
	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.setMnemonic(KeyEvent.VK_E);
		
		JMenuItem undo = addMenuItem(actions, menu, ActionUndo.class, KeyEvent.VK_U);
		setControlKeyAccelerator(undo, 'Z');
		JMenuItem redo = addMenuItem(actions, menu, ActionRedo.class, KeyEvent.VK_R);
		setControlKeyAccelerator(redo, 'Y');
		menu.addSeparator();
		
		JMenuItem cut = addMenuItem(actions, menu, ActionCut.class, KeyEvent.VK_T);
		setControlKeyAccelerator(cut, 'X');
		JMenuItem copy = addMenuItem(actions, menu, ActionCopy.class, KeyEvent.VK_C);
		setControlKeyAccelerator(copy, 'C');
		JMenuItem paste = addMenuItem(actions, menu, ActionPaste.class, KeyEvent.VK_P);
		setControlKeyAccelerator(paste, 'V');
		
		if(isDiagramView())
		{
			addMenuItem(actions, menu, ActionPasteFactorContent.class, KeyEvent.VK_F);
			addMenuItem(actions, menu, ActionPasteWithoutLinks.class, -1);
		}
		
		menu.addSeparator();

		JMenuItem delete = addMenuItem(actions, menu, ActionDelete.class, KeyEvent.VK_DELETE);
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));	
		if(isPlanningView())
		{
			addMenuItem(actions, menu, ActionDeletePlanningViewTreeNode.class);
		}
		if(isViabilityView())
		{
			addMenuItem(actions, menu, ActionDeleteKeyEcologicalAttribute.class);
			addMenuItem(actions, menu, ActionDeleteKeyEcologicalAttributeIndicator.class);
			addMenuItem(actions, menu, ActionDeleteKeyEcologicalAttributeMeasurement.class);
		}
		menu.addSeparator();
		
		JMenuItem selectAll = addMenuItem(actions, menu, ActionSelectAll.class, KeyEvent.VK_A);
		setControlKeyAccelerator(selectAll, 'A');
		
		if(isDiagramView())
		{
			addMenuItem(actions, menu, ActionSelectChain.class, KeyEvent.VK_H);
			menu.addSeparator();
		}
		
		addMenuItem(actions, menu, ActionPreferences.class, KeyEvent.VK_P);
		return menu;
	}
	
	private JMenu createActionsMenu(Actions actions)
	{
		if(isDiagramView())
			return createDiagramActionsMenu(actions);
		
		if(isPlanningView())
			return createPlanningActionsMenu(actions);
		
		if(isViabilityView())
			return createViabilityActionsMenu(actions);
		
		if (isWorkPlanView())
			return createWorkPlanActionsMenu(actions);
		
		return null;
	}

	private JMenu createEmptyActionsMenu()
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Actions"));
		menu.setMnemonic(KeyEvent.VK_A);
		return menu;
	}

	private JMenu createDiagramActionsMenu(Actions actions)
	{
		JMenu menu = createEmptyActionsMenu();
		
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDraftStrategy.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertStrategy.class),KeyEvent.VK_S));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertContributingFactor.class),KeyEvent.VK_C));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIntermediateResult.class),KeyEvent.VK_R));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertThreatReductionResult.class),KeyEvent.VK_R));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertScopeBox.class), KeyEvent.VK_B));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class),KeyEvent.VK_T));
		if (isHumanWelfareTargetMode())
		{
			menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertHumanWelfareTarget.class), KeyEvent.VK_H));
		}
		
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTextBox.class), KeyEvent.VK_X));
		menu.addSeparator();

		menu.add(createGroupBoxMenu(actions));
		menu.addSeparator();

		addMenuItem(actions, menu, ActionInsertLink.class, KeyEvent.VK_I);
		addMenuItem(actions, menu, ActionCreateBendPoint.class, KeyEvent.VK_B);
		addMenuItem(actions, menu, ActionDeleteBendPoint.class, KeyEvent.VK_DELETE);
		
		addMenuItem(actions, menu, ActionCreateIncomingJunction.class, KeyEvent.VK_J);
		addMenuItem(actions, menu, ActionCreateOutgoingJunction.class, KeyEvent.VK_O);
		addMenuItem(actions, menu, ActionManageFactorTagsFromMenu.class, KeyEvent.VK_G);
		
		menu.addSeparator();

		addMenuItem(actions, menu, ActionShowConceptualModel.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionShowResultsChain.class, KeyEvent.VK_R);
		addMenuItem(actions, menu, ActionCreateResultsChain.class);
		addMenuItem(actions, menu, ActionRenameResultsChain.class);
		addMenuItem(actions, menu, ActionDeleteResultsChain.class);
		
		menu.addSeparator();

		addMenuItem(actions, menu, ActionCreateConceptualModel.class);
		addMenuItem(actions, menu, ActionRenameConceptualModel.class);
		if(Miradi.isDemoMode())
		{
			addMenuItem(actions, menu, ActionArrangeConceptualModel.class);
		}
		addMenuItem(actions, menu, ActionDeleteConceptualModel.class);

		menu.addSeparator();

		addMenuItem(actions, menu, ActionDiagramProperties.class);
		
		return menu;
	}

	private boolean isHumanWelfareTargetMode()
	{
		return getMainWindow().getProject().getMetadata().isHumanWelfareTargetMode();
	}

	private JMenu createPlanningActionsMenu(Actions actions)
	{
		JMenu menu = createEmptyActionsMenu();
		
		addMenuItem(actions, menu, ActionTreeCreateObjective.class);
		addMenuItem(actions, menu, ActionTreeCreateIndicator.class);

		menu.addSeparator();

		addMenuItem(actions, menu, ActionTreeCreateActivity.class);
		addMenuItem(actions, menu, ActionTreeShareActivity.class);

		menu.addSeparator();

		addMenuItem(actions, menu, ActionTreeCreateMethod.class);
		addMenuItem(actions, menu, ActionTreeShareMethod.class);

		menu.addSeparator();

		addMenuItem(actions, menu, ActionCreateChildTask.class);
		addMenuItem(actions, menu, ActionCreateSameLevelTask.class);
		
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionTreeNodeUp.class);
		addMenuItem(actions, menu, ActionTreeNodeDown.class);

		return menu;
	}
	
	private JMenu createWorkPlanActionsMenu(Actions actions)
	{
		JMenu menu = createEmptyActionsMenu();
		
		addMenuItem(actions, menu, ActionTreeCreateActivity.class);
		addMenuItem(actions, menu, ActionTreeShareActivity.class);

		menu.addSeparator();
		addMenuItem(actions, menu, ActionTreeCreateMethod.class);
		addMenuItem(actions, menu, ActionTreeShareMethod.class);

		menu.addSeparator();
		addMenuItem(actions, menu, ActionCreateChildTask.class);
		addMenuItem(actions, menu, ActionCreateSameLevelTask.class);
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionTreeNodeUp.class);
		addMenuItem(actions, menu, ActionTreeNodeDown.class);
		addMenuItem(actions, menu, ActionDeletePlanningViewTreeNode.class);

		return menu;
	}

	private JMenu createViabilityActionsMenu(Actions actions)
	{
		JMenu menu = createEmptyActionsMenu();
		
		addMenuItem(actions, menu, ActionCreateKeyEcologicalAttribute.class);
		addMenuItem(actions, menu, ActionCreateKeyEcologicalAttributeIndicator.class);
		addMenuItem(actions, menu, ActionCreateKeyEcologicalAttributeMeasurement.class);

		return menu;
	}

	private boolean isWelcomeView()
	{
		if(mainWindow == null)
			return false;
		
		return NoProjectView.is(mainWindow.getCurrentView());
	}

	private boolean isPlanningView()
	{
		if(mainWindow == null)
			return false;
		
		return PlanningView.is(mainWindow.getCurrentView());
	}
	
	private boolean isWorkPlanView()
	{
		if(mainWindow == null)
			return false;
		
		return WorkPlanView.is(mainWindow.getCurrentView());
	}

	private boolean isDiagramView()
	{
		if(mainWindow == null)
			return false;
		
		return DiagramView.is(mainWindow.getCurrentView());
	}

	private boolean isViabilityView()
	{
		if(mainWindow == null)
			return false;
		
		return TargetViabilityView.is(mainWindow.getCurrentView());
	}

	private boolean isThreatView()
	{
		if(mainWindow == null)
			return false;
		
		return ThreatMatrixView.is(mainWindow.getCurrentView());
	}

	private JMenu createGroupBoxMenu(Actions actions)
	{
		UiMenu groupBoxMenu = new UiMenu(EAM.text("Menu|Group Box"));	
		groupBoxMenu.setMnemonic(KeyEvent.VK_G);		
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionInsertGroupBox.class), KeyEvent.VK_G));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionGroupBoxAddFactor.class), KeyEvent.VK_R));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionGroupBoxRemoveFactor.class), KeyEvent.VK_P));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionDeleteGroupBox.class), KeyEvent.VK_D));
			
		return groupBoxMenu;
	}
		
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		menu.setMnemonic(KeyEvent.VK_V);
		
		Action[] viewSwitchActions = ViewSwitcher.getViewSwitchActions(actions);
		for(int i = 0; i < viewSwitchActions.length; ++i)
			menu.add(viewSwitchActions[i]);
		menu.addSeparator();
// NOTE: Slide show disabled for 1.0.6 release because it is not ready yet
//		addMenuItem(actions, menu, ActionToggleSlideShowPanel.class, KeyEvent.VK_E);
//		addMenuItem(actions, menu, ActionSlideShowViewer.class, KeyEvent.VK_S);
//		menu.addSeparator();
		
		if(isDiagramView())
		{
			JMenuItem zoomIn = addMenuItem(actions, menu, ActionZoomIn.class, KeyEvent.VK_I);
			setControlKeyAccelerator(zoomIn, '=');
			JMenuItem zoomOut = addMenuItem(actions, menu, ActionZoomOut.class, KeyEvent.VK_O);
			setControlKeyAccelerator(zoomOut, '-');
			JMenuItem zoomToFit = addMenuItem(actions, menu, ActionZoomToFit.class, KeyEvent.VK_Z);
			setControlKeyAccelerator(zoomToFit, '0');
			JMenuItem createMargin = addMenuItem(actions, menu, ActionCreateDiagramMargin.class, KeyEvent.VK_M);
			setControlKeyAccelerator(createMargin, 'M');		
	
			menu.addSeparator();
		}
		
		if(isDiagramView())
		{
			addMenuItem(actions, menu, ActionConfigureLayers.class, KeyEvent.VK_C);
			addMenuItem(actions, menu, ActionShowSelectedChainMode.class, KeyEvent.VK_S);
			addMenuItem(actions, menu, ActionShowFullModelMode.class, KeyEvent.VK_F);
		}
		
		if(isPlanningView() || isViabilityView() || isWorkPlanView())
		{
			addMenuItem(actions, menu, ActionExpandAllRows.class);
			addMenuItem(actions, menu, ActionCollapseAllRows.class);
		}
		if(isWorkPlanView())
		{
			addMenuItem(actions, menu, ActionWorkPlanBudgetCustomizeTableEditor.class);
			addMenuItem(actions, menu, ActionFilterWorkPlanByProjectResource.class);
		}
		if (isViabilityView())
		{
			addMenuItem(actions, menu, ActionExpandToTarget.class);
			if (isHumanWelfareTargetMode())
				addMenuItem(actions, menu, ActionExpandToHumanWelfareTarget.class);
			
			addMenuItem(actions, menu, ActionExpandToKeyEcologicalAttribute.class);
			addMenuItem(actions, menu, ActionExpandToIndicator.class);
			addMenuItem(actions, menu, ActionExpandToGoal.class);
			addMenuItem(actions, menu, ActionExpandToMeasurement.class);
		}
		
		if(isThreatView())
		{
			addMenuItem(actions, menu, ActionShowCellRatings.class);
			addMenuItem(actions, menu, ActionHideCellRatings.class);
		}
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionInvokeDashboard.class);
		
		return menu;
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		try
		{
			JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-Step"));
			menu.setMnemonic(KeyEvent.VK_S);

			menu.add(createQuestionBasedMenu(actions, new OpenStandardsConceptualizeQuestion()));
			menu.add(createQuestionBasedMenu(actions, new OpenStandardsPlanActionsAndMonitoringQuestion()));
			menu.add(createQuestionBasedMenu(actions, new OpenStandardsImplementActionsAndMonitoringQuestion()));
			menu.add(createQuestionBasedMenu(actions, new OpenStandardsAnalyzeUseAndAdaptQuestion()));
			menu.add(createQuestionBasedMenu(actions, new OpenStandardsCaptureAndShareLearningQuestion()));
			menu.add(new JMenuItem(actions.get(ActionJumpCloseTheLoop.class)));

			if (Miradi.isDeveloperMode())
				addMenuItem(actions, menu, ActionShowCurrentWizardFileName.class, KeyEvent.VK_S);

			return menu;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
			
			return null;
		}
	}

	public JMenu createQuestionBasedMenu(Actions actions, DynamicChoiceWithRootChoiceItem question) throws Exception
	{
		ChoiceItem headerChoiceItem  = question.getHeaderChoiceItem();
		AbstractJumpMenuAction action = actions.getJumpMenuAction(headerChoiceItem.getCode());
		JMenu headerMenu = new JMenu(headerChoiceItem.getLabel());
		headerMenu.setMnemonic(action.getMnemonic());
		addSubMenus(actions, headerMenu, headerChoiceItem.getChildren());
		
		return headerMenu;
	}
	
	private void addSubMenus(Actions actions, JMenu headerMenu, Vector<ChoiceItem> children)
	{
		for(ChoiceItem subHeaderChoiceItem : children)
		{
			JMenu menu = new JMenu(subHeaderChoiceItem.getLabel());
			String code = subHeaderChoiceItem.getCode();
			AbstractJumpMenuAction action = actions.getJumpMenuAction(code);
			menu.setMnemonic(action.getMnemonic());
			headerMenu.add(menu);
			ChoiceItemWithChildren leafChildren = (ChoiceItemWithChildren) subHeaderChoiceItem;
			addLeafMenus(actions, menu, leafChildren.getChildren());
		}
	}

	private void addLeafMenus(Actions actions, JMenu subHeaderMenu, Vector<ChoiceItem> leafChildren)
	{
		for(ChoiceItem leafChoiceItem : leafChildren)
		{
			AbstractJumpMenuAction action = actions.getJumpMenuAction(leafChoiceItem.getCode());
			if (action == null)
			{
				JMenuItem disabledMenuItem = new JMenuItem(leafChoiceItem.getLabel());
				disabledMenuItem.setEnabled(false);
				subHeaderMenu.add(disabledMenuItem);
			}
			else
			{
				subHeaderMenu.add(new MiradiMenuItem(action, action.getMnemonic()));
			}
		}
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem item  = addMenuItem(actions, menu, ActionHelpButtonMoreInfo.class, KeyEvent.VK_I);
		item.putClientProperty(HelpButtonData.class, 
				new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.MORE_INFO, HelpButtonData.MORE_INFO_HTML));
		
		item = addMenuItem(actions, menu, ActionHelpButtonExamples.class, KeyEvent.VK_E);
		item.putClientProperty(HelpButtonData.class, 
				new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.EXAMPLES, HelpButtonData.EXAMPLES_HTML));
		
		item  = addMenuItem(actions, menu, ActionHelpButtonWorkshop.class, KeyEvent.VK_W);
		item.putClientProperty(HelpButtonData.class, 
				new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.WORKSHOP, HelpButtonData.WORKSHOP_HTML));
		
		menu.addSeparator();
		
		item  = addMenuItem(actions, menu, ActionHelpCMPStandards.class, KeyEvent.VK_O);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.CMP_STANDARDS,HelpButtonData.CMP_STANDARDS_HTML));


		item  = addMenuItem(actions, menu, ActionHelpAdaptiveManagement.class, KeyEvent.VK_M);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.ADAPTIVE_MANAGEMENT,HelpButtonData.ADAPTIVE_MANAGEMENT_HTML));

		
		item  = addMenuItem(actions, menu, ActionHelpAgileSoftware.class, KeyEvent.VK_S);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.AGILE_SOFTWARE,HelpButtonData.AGILE_SOFTWARE_HTML));

		menu.addSeparator();
		
		item  = addMenuItem(actions, menu, ActionHelpComingAttractions.class, KeyEvent.VK_T);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.COMING_ATTACTIONS, HelpButtonData.COMING_ATTRACTIONS_HTML));

		
		item  = addMenuItem(actions, menu, ActionHelpCredits.class, KeyEvent.VK_R);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.CREDITS,HelpButtonData.CREDITS_HTML));
		
		item  = addMenuItem(actions, menu, ActionAboutBenetech.class, KeyEvent.VK_B);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.ABOUT_BENETECH,HelpButtonData.ABOUT_BENETECH_HTML));

		item  = addMenuItem(actions, menu, ActionAboutCMP.class, KeyEvent.VK_C);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.ABOUT_CMP,HelpButtonData.ABOUT_CMP_HTML));

		menu.addSeparator();
		
		item  = addMenuItem(actions, menu, ActionHelpButtonSupport.class, KeyEvent.VK_P);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.SUPPORT, HelpButtonData.SUPPORT_HTML));
		
		addMenuItem(actions, menu, ActionAbout.class, KeyEvent.VK_A);
		
		
		return menu;
	}
	
	
	private JMenuItem addMenuItem(Actions actions, JMenu menu, Class class1)
	{
		MiradiMenuItem menuItemNewProject = new MiradiMenuItem(actions.get(class1));
		menu.add(menuItemNewProject);
		return menuItemNewProject; 
	}
	
	private JMenuItem addMenuItem(Actions actions, JMenu menu, Class class1, int mnemonic)
	{
		MiradiMenuItem menuItemNewProject = new MiradiMenuItem(actions.get(class1), mnemonic);
		menu.add(menuItemNewProject);
		return menuItemNewProject; 
	}
	
	
	private JMenuItem createJMenuItemCenterLocation(MiradiAction action, int mnemonic)
	{
		JMenuItem centeredLocationAction = new MenuItemWithoutLocation(action);
		centeredLocationAction.setMnemonic(mnemonic);
		return centeredLocationAction;
	}
	
	private void setControlKeyAccelerator(JMenuItem menuItem, char keyLetter)
	{
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyLetter, InputEvent.CTRL_DOWN_MASK));
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;

}
