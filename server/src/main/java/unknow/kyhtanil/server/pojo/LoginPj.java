/*******************************************************************************
 * Copyright (c) 2014 Unknow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 *     Unknow - initial API and implementation
 ******************************************************************************/
package unknow.kyhtanil.server.pojo;

import java.io.*;

/**
 * 
 * @author Unknow
 */
public class LoginPj implements Serializable
	{
	private static final long serialVersionUID=1L;
	public String name;
	public int id;
	public int lvl;
	
	public LoginPj() {};
	public LoginPj(int i, String n, int l)
		{
		name=n;
		id=i;
		lvl=l;
		}
	}

