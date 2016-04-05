package unknow.kyhtanil.client.screen;

import com.kotcrab.vis.ui.widget.*;

public class CharCreateScreen extends GameScreen
	{
	public CharCreateScreen() throws NoSuchFieldException, SecurityException
		{
		VisTable table=new VisTable();
		stage.addActor(table);

		table.setFillParent(true);
		table.row();
		VisTable t2=new VisTable();
		table.add(t2).center();

		}
	}
