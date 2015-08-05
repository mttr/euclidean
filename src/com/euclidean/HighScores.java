package com.euclidean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

public class HighScores extends BaseObject {
	private static HighScores currentInstance;
	
	private Array<ScoreEntry> scoreArray;
	private BitmapFont bitmapFont;
	private String scoreString;
	private ScoreEntry lastEntry;
	
	public static class ScoreEntry implements Comparable<ScoreEntry> {
		public String playerName;
		public int score;
		
		@Override
		public int compareTo(ScoreEntry entry) {
			if (this.score < entry.score) return 1;
			else if (this.score == entry.score) return 0;
			else return -1;
		}
	}
	
	public static class TextInputListener implements Input.TextInputListener
	{
		@Override
		public void canceled() {
		}

		@Override
		public void input(String text) {
			ScoreEntry entry = (ScoreEntry) sys.gameState.getData();
			
			entry.playerName = text;
			
			HighScores.currentInstance.addEntry(entry, true);
			
			sys.keyboard.flushKeys();
		}		
	}
	
	public HighScores() {
		bitmapFont = new BitmapFont();
		scoreArray = new Array<ScoreEntry>();
		
		readData();
		
		currentInstance = this;
	}
	
	public void addEntry(ScoreEntry entry) {
		scoreArray.add(entry);
		updateScoreString();
	}
	
	public void addEntry(ScoreEntry entry, boolean newEntry) {
		if (newEntry) lastEntry = entry;
		addEntry(entry);
	}

	@Override
	public void update(BaseObject parent)
	{
		if (scoreString == null)
		{
			updateScoreString();
		}
		
		bitmapFont.drawMultiLine(sys.spriteBatch, scoreString, 0, sys.screenHeight - 30);
	}

	private void updateScoreString() {
		scoreString = new String("Press ESC to return to menu.\n\n");
		scoreArray.sort();
		
		boolean inTopTen = false;
		
		for (int i = 0; i < scoreArray.size; i++)
		{
			if (i == 10)
			{
				break;
			}
			
			ScoreEntry entry = scoreArray.get(i);
			
			if (entry == lastEntry)
			{
				scoreString += "* ";
				inTopTen = true;
			}
			
			scoreString += Integer.toString(i + 1) + " ";
			scoreString += entry.playerName + "    ";
			scoreString += Integer.toString(entry.score) + "\n";
		}
		
		if (!inTopTen && lastEntry != null)
		{
			scoreString += "\n*";
			scoreString += lastEntry.playerName + "    ";
			scoreString += Integer.toString(lastEntry.score) + "\n";
		}
	}

	@Override
	public void reset() {
		writeData();
		scoreArray = null;
		bitmapFont.dispose();
		currentInstance = null;
	}
	
	@SuppressWarnings("deprecation")
	public void readData() {
		FileHandle file = Gdx.files.external("euclideanscores");
		
		if (!file.exists()) return;
		
		DataInputStream in = new DataInputStream(file.read());
		
		try
		{
			int count = 0;
			
			while (true)
			{
				ScoreEntry entry = new ScoreEntry();
					
				entry.playerName = in.readLine();
				
				entry.score = in.readInt();
				
				if (entry.playerName == null) continue;
				
				entry.playerName = entry.playerName.replaceAll("\u0000", "");
				
				scoreArray.add(entry);
				
				count++;
				
				if (count == 10)
				{
					break;
				}
			}
		}
		catch (EOFException e)
		{
		}
		catch (IOException e)
		{
			System.out.print(e);
		}
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void writeData() {
		FileHandle file = Gdx.files.external("euclideanscores");
		file.delete();
		
		DataOutputStream out = new DataOutputStream(file.write(false));
		
		try
		{
			for (int i = 0; i < scoreArray.size; i++)
			{
				ScoreEntry entry = scoreArray.get(i);
				out.writeChars(entry.playerName + "\n");
				out.writeInt(entry.score);
			}
			out.close();
		}
		catch (IOException e)
		{
			System.out.print(e);
		}
		
	}
}
