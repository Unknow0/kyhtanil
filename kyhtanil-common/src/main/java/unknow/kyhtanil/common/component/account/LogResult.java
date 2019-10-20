package unknow.kyhtanil.common.component.account;

import javax.annotation.Generated;

import com.artemis.Component;

import unknow.kyhtanil.common.pojo.CharDesc;
import unknow.kyhtanil.common.pojo.UUID;

@Generated(value="class unknow.pojo.Generator", date="2017-05-01T19:13:39+0200")
public class LogResult extends Component
	{
	public UUID uuid=null;
	public CharDesc[] characters=null;

	public LogResult()
		{
		}

	public LogResult(UUID uuid, CharDesc[] characters)
		{
		this.uuid=uuid;
		this.characters=characters;
		}

	public String toString()
		{
		return "uuid: "+uuid+", characters: "+characters;
		}
	}