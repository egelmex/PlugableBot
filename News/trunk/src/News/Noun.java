/*	
 * Copyright 2010 Murmew
 * Copyright 2010 Mex (ellism88@gmail.com)
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
 * Noun class
 * 
 * @author Murmew
 * @auther Mex
 */
public class Noun {
	public String word;
	public Perspective perspective;
	public Plurality plurality;

	/**
	 * Constructor for objects of class Noun
	 */
	public Noun(String word, Perspective perspective, Plurality plurality) {
		this.word = word;
		this.perspective = perspective;
		this.plurality = plurality;
	}
}