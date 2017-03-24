

import javax.swing.JFrame;
/**
 *MAIN CLASS HOLDS THE FRAME, THE ADAPTER AND MY PANEL CLASS.
 *THIS IS THE CLASS  THAT RUNS THE WHOLE PROGRAM.
 *
 */
public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("MINESWEEPER =3");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(400, 400);

		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);

		myFrame.setVisible(true);
	}
}