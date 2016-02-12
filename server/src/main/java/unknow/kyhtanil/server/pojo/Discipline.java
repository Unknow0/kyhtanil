/*******************************************************************************
 * Copyright (c) 2014 Unknow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 * Unknow - initial API and implementation
 ******************************************************************************/
package unknow.kyhtanil.server.pojo;

import java.util.*;

import unknow.common.data.*;

public class Discipline
	{
	private String name;
	private String desc;

	private ArraySet<SkillInterface> skills=new ArraySet<>(10);

	public Discipline()
		{
		}

	public Discipline(String n, String d)
		{
		name=n;
		desc=d;
		}

	public void add(SkillInterface s)
		{
		skills.add(s);
		}

	public String name()
		{
		return name;
		}

	public String desc()
		{
		return desc;
		}

	public void setName(String n)
		{
		name=n;
		}

	public void setDesc(String d)
		{
		desc=d;
		}

	public Set<SkillInterface> skills()
		{
		return skills;
		}

	public SkillInterface skill(int i)
		{
		return skills.get(i);
		}
	}
