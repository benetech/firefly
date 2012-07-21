/* base file for now just a name place holder for upcoming code
*/

package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDownloadProject extends MainWindowAction
{
	public ActionDownloadProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action| Download a project from a database");
	}

	@Override
	public String getToolTipText()
	{
		return EAM.text("Download a project from a specified database");
	}

}