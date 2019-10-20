package unknow.kyhtanil.common.util;

import java.security.NoSuchAlgorithmException;

import unknow.common.kryo.KryoWrap;
import unknow.kyhtanil.common.component.ErrorComp;
import unknow.kyhtanil.common.component.StatPerso;
import unknow.kyhtanil.common.component.StatShared;
import unknow.kyhtanil.common.component.account.CreateAccount;
import unknow.kyhtanil.common.component.account.LogChar;
import unknow.kyhtanil.common.component.account.LogResult;
import unknow.kyhtanil.common.component.account.Login;
import unknow.kyhtanil.common.component.account.PjInfo;
import unknow.kyhtanil.common.component.net.Attack;
import unknow.kyhtanil.common.component.net.DamageReport;
import unknow.kyhtanil.common.component.net.Despawn;
import unknow.kyhtanil.common.component.net.Move;
import unknow.kyhtanil.common.component.net.Spawn;
import unknow.kyhtanil.common.component.net.UpdateInfo;
import unknow.kyhtanil.common.pojo.Point;

public class Kryos extends KryoWrap
	{
	public Kryos() throws NoSuchAlgorithmException
		{
		addClass(ErrorComp.class);
		addClass(Login.class);
		addClass(CreateAccount.class);
		addClass(LogResult.class);
		addClass(LogChar.class);
		addClass(PjInfo.class);
		addClass(Spawn.class);
		addClass(Despawn.class);
		addClass(Move.class);
		addClass(UpdateInfo.class);
		addClass(Attack.class);
		addClass(Point.class);
		addClass(DamageReport.class);
		addClass(StatPerso.class);
		addClass(StatShared.class);
		doneInit();
		}
	}
