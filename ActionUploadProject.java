/* base file for now just a name place holder for upcoming code
*/

package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionUploadProject extends MainWindowAction
{
	public ActionUploadProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action| Upload a project to a database"");
	}

	@Override
	public String getToolTipText()
	{
		return EAM.text("TT|Upload a current or saved project to a specified database");
	}

}