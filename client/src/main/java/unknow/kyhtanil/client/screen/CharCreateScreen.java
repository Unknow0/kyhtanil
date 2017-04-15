package unknow.kyhtanil.client.screen;

import com.kotcrab.vis.ui.widget.*;

public class CharCreateScreen extends GameScreen
	{
	private VisTextField constitution;
	private VisTextField strength;
	private VisTextField intelligence;
	private VisTextField concentration;
	private VisTextField dexterity;

	public CharCreateScreen() throws NoSuchFieldException, SecurityException
		{
		VisTable table=new VisTable();
		stage.addActor(table);

		table.setFillParent(true);

		constitution=new VisTextField();
		strength=new VisTextField();
		intelligence=new VisTextField();
		concentration=new VisTextField();
		dexterity=new VisTextField();

		table.add(new VisLabel("Constitution"));
		table.add(concentration);

		table.row();
		table.add(new VisLabel("Strength"));
		table.add(strength);

		table.row();
		table.add(new VisLabel("Intelligence"));
		table.add(intelligence);

		table.row();
		table.add(new VisLabel("Concentration"));
		table.add(concentration);
		}
	}
