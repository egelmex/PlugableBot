/*	
 * Copyright 2010 Murmew
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package News;

/**
 * Phrase class
 * @author Murmew
 */
public class Phrase
{
	public String present;
	public String past;
	public String active;
	public String object;

	/**
	 * Constructor for objects of class Phrase
	 */
	public Phrase(String present, String past, String active, String object)
	{
		this.present = present;
		this.past = past;
		this.active = active;
		this.object = object;
	}
}