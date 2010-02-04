/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/
Copyright Martin Ellis, 2009
This file is part of PieSpy.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: Graph.java,v 1.38 2004/05/11 15:07:04 pjm2 Exp $

 */

package org.jibble.socnet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import AndrewCassidy.PluggableBot.PluggableBot;

/**
 * The Graph stores the Nodes and Edges, and InferenceHeurisics to allow the
 * structure of the graph to be modified.
 */
public class Graph implements java.io.Serializable {
	private static final long serialVersionUID = 4032536645319340095L;

	private String _label;
	private String _caption = "";
	private Map<Node, Node> _nodes = new HashMap<Node, Node>();
	private Map<Edge, Edge> _edges = new HashMap<Edge, Edge>();

	private double minX = Double.POSITIVE_INFINITY;
	private double maxX = Double.NEGATIVE_INFINITY;
	private double minY = Double.POSITIVE_INFINITY;
	private double maxY = Double.NEGATIVE_INFINITY;
	private double maxWeight = 0;

	private Configuration config;
	private int _frameCount = 0;
	private File _lastFile = null;

	private List<InferenceHeuristic> _heuristics = new ArrayList<InferenceHeuristic>();

	private NumberFormat _nf;

	/**
	 * 
	 * @param label
	 * @param config
	 */
	public Graph(String label, Configuration config) {
		_label = label;
		this.config = config;

		// The NumberFormat used to format frame numbers in filenames.
		_nf = NumberFormat.getIntegerInstance();
		_nf.setMinimumIntegerDigits(8);
		_nf.setGroupingUsed(false);

		// Add InferenceHeuristics. These are processed in this order.
		_heuristics.add(new DirectAddressingInferenceHeuristic(this, config));
		_heuristics.add(new IndirectAddressingInferenceHeuristic(this, config));
		_heuristics.add(new AdjacencyInferenceHeuristic(this, config));
		_heuristics.add(new BinarySequenceInferenceHeuristic(this, config));
		_heuristics.add(new KarmaInferenceHeuristic(this, config));

	}

	/**
	 * Pass a message through the list of InferenceHeuristics.
	 * 
	 * @param nick
	 * @param message
	 */
	public void infer(String nick, String message) {
		if (config.ignoreSet.contains(nick.toLowerCase())) {
			return;
		}

		for (int i = 0; i < _heuristics.size(); i++) {
			InferenceHeuristic heuristic = (InferenceHeuristic) _heuristics
					.get(i);
			heuristic.infer(nick, message);
		}
	}

	/**
	 *  Add a Node to the Graph.
	 * @param node
	 */
	public void addNode(Node node) {

		// Only add the Node to the HashMap if it's not already in there.
		if (_nodes.containsKey(node)) {
			node = _nodes.get(node);
		} else {
			_nodes.put(node, node);
		}

		// Increment the weight of the Node.
		node.setWeight(node.getWeight() + 1);
	}

	/**
	 * Add an Edge to the Graph. Increment the weighting if it already exists.
	 * 
	 * @param source
	 * @param target
	 * @param weight
	 * @return
	 */
	public boolean addEdge(Node source, Node target, double weight) {
		// Do not add self-edges or weights that are not positive.
		if (source.equals(target) || weight <= 0) {
			return false;
		}

		// Ensure both Nodes are in the Graph first.
		addNode(source);
		addNode(target);

		// Add the Edge to the HashMap, or find the existing entry.
		Edge edge = new Edge(source, target);
		if (_edges.containsKey(edge)) {
			edge = _edges.get(edge);
		} else {
			source = _nodes.get(source);
			target = _nodes.get(target);
			edge = new Edge(source, target);
			_edges.put(edge, edge);
		}
		// Increment the edge weight.
		edge.setWeight(edge.getWeight() + weight);

		// The graph has changed.
		makeNextImage();
		return true;
	}

	/**
	 * Sets karama on an edge
	 * 
	 * @param source
	 * @param target
	 * @param karma
	 * @return
	 */
	public boolean addKaramEdge(Node source, Node target, int karma) {
		// Do not add self-edges or weights that are not positive.
		if (source.equals(target)) {
			return false;
		}

		// Ensure both Nodes are in the Graph first.
		addNode(source);
		addNode(target);

		// Add the Edge to the HashMap, or find the existing entry.
		Edge edge = new Edge(source, target);
		if (_edges.containsKey(edge)) {
			edge = _edges.get(edge);
		} else {
			source = _nodes.get(source);
			target = _nodes.get(target);
			edge = new Edge(source, target);
			_edges.put(edge, edge);
		}
		// Increment the edge weight.
		edge.setKarma(edge.getKarma() + karma);

		// The graph has changed.
		makeNextImage();
		return true;
	}

	/**
	 * Remove a Node from the Graph, along with all of its emanating Edges.
	 * 
	 * @param node
	 * @return
	 */
	public boolean removeNode(Node node) {
		if (_nodes.containsKey(node)) {
			// Remove the Node from the HashMap.
			_nodes.remove(node);

			// Remove all Edges that connect to the removed Node.
			Iterator<Edge> edgeIt = _edges.keySet().iterator();
			while (edgeIt.hasNext()) {
				Edge edge = edgeIt.next();
				if (edge.getSource().equals(node)
						|| edge.getTarget().equals(node)) {
					edgeIt.remove();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Merges oldNode and newNode by: 1) Removing newNode from the Graph and
	 * removing all emanating Edges. 2) Renaming the nick of oldNode to match
	 * the nick of newNode.
	 * 
	 * @param oldNode
	 * @param newNode
	 */
	public void mergeNode(Node oldNode, Node newNode) {
		if (!this.contains(oldNode)) {
			// If the oldNode is not in this Graph, we needn't do anything.
			return;
		}

		// Get the nick that we want to change to.
		String nick = newNode.toString();

		// Remove the newNode from the Graph (and any emanating Edges).
		// Warning: Some nick changes only differ in case, so make sure
		// we're not inadvertantly removing oldNick, too!
		if (!newNode.equals(oldNode)) {
			removeNode(newNode);
		}

		// Important: HashMap keys are allocated to locations based on their
		// hashCode values when they are added. Such keys should ideally be
		// immutable, as they are assumed to return the same hashCode every
		// time.
		// Changing the nick of a Node object causes this contract to be
		// broken, which allows the HashMap to store duplicate Node keys.
		// This makes it necessary to rename a Node by first removing it
		// from the HashMap, then changing its nick, before finally putting
		// it back into the HashMap.
		// Likewise, the hashCode method for Edge depends on the hashCode
		// of its two Nodes, so all Edges emanating from the Node must
		// be removed from the HashMap before the Node is renamed. These must
		// then be put back into the HashMap after the Node has been renamed.

		// Create a List of Edges that will be affected by renaming oldNode.
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		List<Edge> changedEdges = new LinkedList<Edge>();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();
			if (edge.getSource().equals(oldNode)
					|| edge.getTarget().equals(oldNode)) {
				changedEdges.add(edge);
			}
		}

		// Remove all affected edges from the HashMap.
		Iterator<Edge> changedIt = changedEdges.iterator();
		while (changedIt.hasNext()) {
			Edge edge = changedIt.next();
			_edges.remove(edge);
		}

		// Remove, rename and replace the oldNode in the HashMap.
		oldNode = get(oldNode);
		_nodes.remove(oldNode);
		oldNode.setNick(nick);
		_nodes.put(oldNode, oldNode);

		// Put all the affected edges back into the HashMap.
		changedIt = changedEdges.iterator();
		while (changedIt.hasNext()) {
			Edge edge = (Edge) changedIt.next();
			_edges.put(edge, edge);
		}

		if (getConnectedNodes().contains(oldNode)) {
			// The changed node is in the graph, so it needs redrawing.
			makeNextImage();
		}

	}

	/**
	 * Return true if the Graph contains the Node. (This does not necessarily
	 * imply that the Node is visible).
	 * 
	 * @param node
	 * @return
	 */
	public boolean contains(Node node) {
		return _nodes.containsKey(node);
	}

	/**
	 * Return true if the Graph contains the Edge.
	 * 
	 * @param edge
	 * @return
	 */
	public boolean contains(Edge edge) {
		return _edges.containsKey(edge);
	}

	/**
	 * Return the Graph's Node that has the same nick as the supplied Node.
	 */
	public Node get(Node node) {
		return (Node) _nodes.get(node);
	}

	/**
	 * Return the Graph's Edge that matched the supplied Edge.
	 * 
	 * @param edge
	 * @return
	 */
	public Edge get(Edge edge) {
		return (Edge) _edges.get(edge);
	}

	public String toString() {
		return "Graph: " + _nodes.size() + " nodes and " + _edges.size()
				+ " edges.";
	}

	public String toString2() {
		return "Nodes:\n" + _nodes + "\nEdges:\n" + _edges;
	}

	/**
	 * Apply the temporal decay to the Graph.
	 * 
	 * @param amount
	 */
	public void decay(double amount) {

		// Decrease all Edge weightings and remove non-positive Edges.
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();
			edge.setWeight(edge.getWeight() - amount);
			if (edge.getWeight() <= 0) {
				edgeIt.remove();
			}
		}

		// Decrease all Node weightings, but do not allow them to be negative.
		Iterator<Node> nodeIt = _nodes.keySet().iterator();
		while (nodeIt.hasNext()) {
			Node node = nodeIt.next();
			node.setWeight(node.getWeight() - amount);
			if (node.getWeight() < 0) {
				node.setWeight(0);
			}
		}

	}

	/**
	 * 
	 */
	public void decayKarma() {
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();
			if (edge.getKarma() > 0) {
				double newKarma = edge.getKarma() - 0.3;
				edge.setKarma(newKarma >= 0 ? newKarma : 0.0);
			} else if (edge.getKarma() < 0) {
				double newKarma = edge.getKarma() + 0.3;
				edge.setKarma(newKarma <= 0 ? newKarma : 0.0);
			}
		}
	}

	/**
	 *  Returns the set of all Nodes that have emanating Edges.
	 *  This therefore returns all Nodes that will be visible in the drawing.
	 * @return
	 */
	private Set<Node> getConnectedNodes() {
		Set<Node> connectedNodes = new HashSet<Node>();
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();
			connectedNodes.add(edge.getSource());
			connectedNodes.add(edge.getTarget());
		}
		return connectedNodes;
	}

	/**
	 *  Applies the spring embedder.
	 * @param iterations
	 */
	public void doLayout(int iterations) {

		// For performance, copy each set into an array.
		Set<Node> visibleNodes = getConnectedNodes();
		Node[] nodes = (Node[]) visibleNodes.toArray(new Node[visibleNodes
				.size()]);
		Edge[] edges = (Edge[]) _edges.keySet()
				.toArray(new Edge[_edges.size()]);

		double k = config.k;
		double c = config.c;
		// Repulsive forces between nodes that are further apart than this are
		// ignored.
		double maxRepulsiveForceDistance = config.maxRepulsiveForceDistance;

		// For each iteration...
		for (int it = 0; it < iterations; it++) {

			// Calculate forces acting on nodes due to node-node repulsions...
			for (int a = 0; a < nodes.length; a++) {
				for (int b = a + 1; b < nodes.length; b++) {
					Node nodeA = nodes[a];
					Node nodeB = nodes[b];

					double deltaX = nodeB.getX() - nodeA.getX();
					double deltaY = nodeB.getY() - nodeA.getY();

					double distanceSquared = deltaX * deltaX + deltaY * deltaY;

					if (distanceSquared < 0.01) {
						deltaX = Math.random() / 10 + 0.1;
						deltaY = Math.random() / 10 + 0.1;
						distanceSquared = deltaX * deltaX + deltaY * deltaY;
					}

					double distance = Math.sqrt(distanceSquared);

					if (distance < maxRepulsiveForceDistance) {
						double repulsiveForce = (k * k / distance);

						nodeB.setFX(nodeB.getFX()
								+ (repulsiveForce * deltaX / distance));
						nodeB.setFY(nodeB.getFY()
								+ (repulsiveForce * deltaY / distance));
						nodeA.setFX(nodeA.getFX()
								- (repulsiveForce * deltaX / distance));
						nodeA.setFY(nodeA.getFY()
								- (repulsiveForce * deltaY / distance));
					}
				}
			}

			/**
			 *  Calculate forces acting on nodes due to edge attractions.
			 */
			for (int e = 0; e < edges.length; e++) {
				Edge edge = edges[e];
				Node nodeA = edge.getSource();
				Node nodeB = edge.getTarget();

				double deltaX = nodeB.getX() - nodeA.getX();
				double deltaY = nodeB.getY() - nodeA.getY();

				double distanceSquared = deltaX * deltaX + deltaY * deltaY;

				// Avoid division by zero error or Nodes flying off to
				// infinity. Pretend there is an arbitrary distance between
				// the Nodes.
				if (distanceSquared < 0.01) {
					deltaX = Math.random() / 10 + 0.1;
					deltaY = Math.random() / 10 + 0.1;
					distanceSquared = deltaX * deltaX + deltaY * deltaY;
				}

				double distance = Math.sqrt(distanceSquared);

				if (distance > maxRepulsiveForceDistance) {
					distance = maxRepulsiveForceDistance;
				}

				distanceSquared = distance * distance;

				double attractiveForce = (distanceSquared - k * k) / k;

				// Make edges stronger if people know each other.
				double weight = edge.getWeight();
				if (weight < 1) {
					weight = 1;
				}
				attractiveForce *= (Math.log(weight) * 0.5) + 1;

				nodeB
						.setFX(nodeB.getFX() - attractiveForce * deltaX
								/ distance);
				nodeB
						.setFY(nodeB.getFY() - attractiveForce * deltaY
								/ distance);
				nodeA
						.setFX(nodeA.getFX() + attractiveForce * deltaX
								/ distance);
				nodeA
						.setFY(nodeA.getFY() + attractiveForce * deltaY
								/ distance);

			}

			/**
			 *  Now move each node to its new location...
			 */
			for (int a = 0; a < nodes.length; a++) {
				Node node = nodes[a];

				double xMovement = c * node.getFX();
				double yMovement = c * node.getFY();

				// Limit movement values to stop nodes flying into oblivion.
				double max = config.maxNodeMovement;
				if (xMovement > max) {
					xMovement = max;
				} else if (xMovement < -max) {
					xMovement = -max;
				}
				if (yMovement > max) {
					yMovement = max;
				} else if (yMovement < -max) {
					yMovement = -max;
				}

				node.setX(node.getX() + xMovement);
				node.setY(node.getY() + yMovement);

				// Reset the forces
				node.setFX(0);
				node.setFY(0);
			}

		}

	}

	/**
	 *  Work out the drawing boundaries...
	 * @param width
	 * @param height
	 */
	public void calcBounds(int width, int height) {

		minX = Double.POSITIVE_INFINITY;
		maxX = Double.NEGATIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;
		maxY = Double.NEGATIVE_INFINITY;
		maxWeight = 0;

		Set<Node> nodes = getConnectedNodes();
		Iterator<Node> nodeIt = nodes.iterator();
		while (nodeIt.hasNext()) {
			Node node = nodeIt.next();

			if (node.getX() > maxX) {
				maxX = node.getX();
			}
			if (node.getX() < minX) {
				minX = node.getX();
			}
			if (node.getY() > maxY) {
				maxY = node.getY();
			}
			if (node.getY() < minY) {
				minY = node.getY();
			}
		}

		// Increase size if too small.
		double minSize = config.minDiagramSize;
		if (maxX - minX < minSize) {
			double midX = (maxX + minX) / 2;
			minX = midX - (minSize / 2);
			maxX = midX + (minSize / 2);
		}
		if (maxY - minY < minSize) {
			double midY = (maxY + minY) / 2;
			minY = midY - (minSize / 2);
			maxY = midY + (minSize / 2);
		}

		// Work out the maximum weight.
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();
			if (edge.getWeight() > maxWeight) {
				maxWeight = edge.getWeight();
			}
		}

		// Jibble the boundaries to maintain the aspect ratio.
		double xyRatio = ((maxX - minX) / (maxY - minY)) / (width / height);
		if (xyRatio > 1) {
			// diagram is wider than it is high.
			double dy = maxY - minY;
			dy = dy * xyRatio - dy;
			minY = minY - dy / 2;
			maxY = maxY + dy / 2;
		} else if (xyRatio < 1) {
			// Diagram is higher than it is wide.
			double dx = maxX - minX;
			dx = dx / xyRatio - dx;
			minX = minX - dx / 2;
			maxX = maxX + dx / 2;
		}

	}

	public BufferedImage drawImage(int width, int height, int borderSize,
			int nodeRadius, double edgeThreshold, boolean showEdges) {

		Set<Node> nodes = getConnectedNodes();

		// Now actually draw the thing...

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		g.setColor(config.backgroundColor);
		g.fillRect(1, 1, width - 2, height - 2);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(config.borderColor);
		g.drawRect(0, 0, width - 1, height - 1);

		width = width - borderSize * 3; // note the 3 (gives more border on
		// right side)
		height = height - borderSize * 2;

		g.setColor(config.channelColor);
		g.setFont(new Font("SansSerif", Font.BOLD, 64));
		g.drawString(_label, borderSize + 20, 80);

		g.setColor(config.titleColor);
		g.setFont(new Font("SansSerif", Font.BOLD, 18));
		g.drawString("A Social Network Diagram for an IRC Channel", borderSize,
				borderSize - nodeRadius - 15);
		g.drawString(_caption, borderSize, height + borderSize * 2 - 5 - 50);
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		g.drawString("Generated by " + PluggableBot.Nick() + " using "
				+ SocialNetworkBot.VERSION, borderSize, height + borderSize * 2
				- 5 - 30);
		g
				.drawString(
						"Blue edge thickness and shortness represents strength of relationship",
						borderSize, height + borderSize * 2 - 5 - 15);
		g.drawString("http://www.jibble.org/piespy/ - This frame was drawn at "
				+ new Date(), borderSize, height + borderSize * 2 - 5);

		// Draw all edges...
		Iterator<Edge> edgeIt = _edges.keySet().iterator();
		while (edgeIt.hasNext()) {
			Edge edge = edgeIt.next();

			if (edge.getWeight() < edgeThreshold) {
				continue;
			}

			double weight = edge.getWeight();

			Node nodeA = edge.getSource();
			Node nodeB = edge.getTarget();
			int x1 = (int) (width * (nodeA.getX() - minX) / (maxX - minX))
					+ borderSize;
			int y1 = (int) (height * (nodeA.getY() - minY) / (maxY - minY))
					+ borderSize;
			int x2 = (int) (width * (nodeB.getX() - minX) / (maxX - minX))
					+ borderSize;
			int y2 = (int) (height * (nodeB.getY() - minY) / (maxY - minY))
					+ borderSize;
			g.setStroke(new BasicStroke(
					(float) (Math.log(weight + 1) * 0.5) + 1));
			int alpha = 102 + (int) (153 * weight / maxWeight);
			if (edge.getKarma() == 0) {
				g.setColor(new Color(config.edgeColor.getRed(),
						config.edgeColor.getGreen(),
						config.edgeColor.getBlue(), alpha));
			} else if (edge.getKarma() > 0) {
				g.setColor(new Color(config.karmaPlusColor.getRed(),
						config.karmaPlusColor.getGreen(), config.karmaPlusColor
								.getBlue(), alpha));
			} else {
				g.setColor(new Color(config.karmaMinusColor.getRed(),
						config.karmaMinusColor.getGreen(),
						config.karmaMinusColor.getBlue(), alpha));
			}
			if (showEdges) {
				g.drawLine(x1, y1, x2, y2);
			}
		}

		// Draw all nodes...
		g.setStroke(new BasicStroke(2.0f));
		g.setFont(new Font("SansSerif", Font.PLAIN, 10));
		Iterator<Node> nodeIt = nodes.iterator();
		while (nodeIt.hasNext()) {
			Node node = nodeIt.next();
			int x1 = (int) (width * (node.getX() - minX) / (maxX - minX))
					+ borderSize;
			int y1 = (int) (height * (node.getY() - minY) / (maxY - minY))
					+ borderSize;
			// int newNodeRadius = (int) Math.log((node.getWeight() + 1) / 10) +
			// nodeRadius;
			g.setColor(config.nodeColor);
			g.fillOval(x1 - nodeRadius, y1 - nodeRadius, nodeRadius * 2,
					nodeRadius * 2);
			g.setColor(config.edgeColor);
			g.drawOval(x1 - nodeRadius, y1 - nodeRadius, nodeRadius * 2,
					nodeRadius * 2);
			// g.setColor(Color.white);
			// g.drawString(node.toString(), x1 + nodeRadius + 1, y1 -
			// nodeRadius);
			// g.drawString(node.toString(), x1 + nodeRadius - 1, y1 -
			// nodeRadius);
			// g.drawString(node.toString(), x1 + nodeRadius, y1 - nodeRadius +
			// 1);
			// g.drawString(node.toString(), x1 + nodeRadius, y1 - nodeRadius -
			// 1);
			g.setColor(config.labelColor);
			g.drawString(node.toString(), x1 + nodeRadius, y1 - nodeRadius);
		}

		return image;
	}

	public int getFrameCount() {
		return _frameCount;
	}

	public String getLabel() {
		return _label;
	}

	public void setCaption(String caption) {
		_caption = caption;
	}

	public void setLastFile(File file) {
		_lastFile = file;
	}

	public File getLastFile() {
		return _lastFile;
	}

	public void makeNextImage() {

		_frameCount++;
		String strippedChannel = _label.toLowerCase().substring(1);

		File dir = new File(config.outputDirectory, strippedChannel);
		dir.mkdirs();

		doLayout(config.springEmbedderIterations);
		calcBounds(config.outputWidth, config.outputHeight);

		try {
			BufferedImage image = drawImage(config.outputWidth,
					config.outputHeight, config.borderSize, config.nodeRadius,
					config.edgeThreshold, config.showEdges);

			// Write the archive image.
			File file = new File(dir, strippedChannel + "-"
					+ _nf.format(_frameCount) + ".png");
			if (config.createArchive) {
				System.out.println("Making frame: " + file);
				ImageIO.write(image, "png", file);
				_lastFile = file;
			}

			// Also save an image as channel-current.png.
			File current = new File(dir, strippedChannel + "-current.png");
			if (config.createCurrent) {
				ImageIO.write(image, "png", current);
				if (!config.createArchive) {
					_lastFile = file;
				}
			}

			// Also serialize the graph object for later retrieval.
			if (config.createRestorePoints) {
				writeGraph();
			}

		} catch (Exception e) {
			System.out.println("PieSpy has gone wibbly: " + e);
			e.printStackTrace();
		}

		// Apply the temporal decay after each frame is created.
		decay(config.temporalDecayAmount);
		decayKarma();
	}

	// Serialize this Graph and write it to a File.
	public void writeGraph() {
		try {

			String strippedChannel = _label.toLowerCase().substring(1);
			File dir = new File(config.outputDirectory, strippedChannel);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, strippedChannel + "-restore.dat");
			System.out.println("Serialize Graph to: " + file);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file));
			oos.writeObject(SocialNetworkBot.VERSION);
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}