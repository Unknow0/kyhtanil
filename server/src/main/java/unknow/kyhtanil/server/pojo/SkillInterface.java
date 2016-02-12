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

public interface SkillInterface
	{
	public static enum Type
		{
		PASSIVE, DIRECT, BUFF, DEBUFF, TOGGLE
		};

	public String name();

	public String description();

	public Type type();

	public Discipline discipline();

	public String action();

	public double speed();
	}
