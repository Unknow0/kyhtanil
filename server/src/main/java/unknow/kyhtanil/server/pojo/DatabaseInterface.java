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

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * 
 * @author Unknow
 */
public interface DatabaseInterface
	{
	/** @return account or -1 if login/pass are incorect */
	public int login(String login, String pass) throws SQLException, IOException;

	/**
	 * Recupere la list des personnage disponible.
	 */
	public LoginPj[] pjList(int account) throws SQLException, IOException;

	/**
	 * verifie si un nom est deja pris.
	 * 
	 * @return true si le nom de perso est deja utiliser.
	 */
	public boolean isExistPjName(String name) throws SQLException, IOException;

	/**
	 * Ajoute le personnage dans la base de donner.
	 * 
	 * @return le personage en base.
	 */
	public PjInterface createPj(int account, PjInterface pj) throws SQLException, IOException;

	/**
	 * Charge un personage depuis la base.
	 */
	public PjInterface loadPj(int account, int id) throws SQLException, IOException;

	/**
	 * recupere la list des skills.
	 */
	public List<SkillInterface> getSkills();

	/**
	 * recupere un skill.
	 */
	public SkillInterface getSkill(int id);

	/**
	 * Recupere la list des disciplines.
	 */
	public List<Discipline> getDisciplines();

	/**
	 * recupere une discipline 
	 */
	public Discipline getDiscipline(int id);
	}
