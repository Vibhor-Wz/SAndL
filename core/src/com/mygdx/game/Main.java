package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helpers.GameInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class Main extends Game {
	private Stage stage;
	Board group;


	float boardHeight,boardXPosition,boardYPosition;

	@Override
	public void create () {

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		//here we can change height and x y position of the board and
		boardHeight=GameInfo.HEIGHT;
		boardXPosition=0;
		boardYPosition= GameInfo.HEIGHT*0.3f;

		Group group2 = new Group();
		group = new Board(boardHeight,boardXPosition,boardYPosition);

		group2.setPosition(boardXPosition,boardYPosition);
		group2.addActor(group);

		int SnakeAndLadderCount =4;

		Table table = new Table();
		Array<Table> a = createLadder(SnakeAndLadderCount);
		for (Table y : a){
			table.addActor(y);
		}
		group2.addActor(table);

		Table table1 = new Table();
		Array<Table> snake = createSnake(SnakeAndLadderCount);
		for (Table z : snake){
			table1.addActor(z);
		}
		group2.addActor(table1);

		stage.addActor(group2);
		System.out.println(ladderMap);


	}

	Map<Integer, Integer> ladderMap = new HashMap<>();
//	public static <K, V> K getKey(Map<K, V> ladderMap, V value)
//	{
//		for (Map.Entry<K, V> entry: ladderMap.entrySet())
//		{
//			if (value.equals(entry.getValue())) {
//				return entry.getKey();
//			}
//		}
//		return null;
//	}

	//creating start cell for ladder with checking there would no more present that start cell for any other ladder
	public int ladderFirstCell(){
		Random ran = new Random();
		int cellFirst = ran.nextInt(78)+1;
		
		Iterator<Map.Entry<Integer, Integer> > iterator = ladderMap.entrySet().iterator();

		boolean isKeyPresent = false;

		// Iterate over the HashMap
		while (iterator.hasNext()) {

			// Get the entry at this iteration
			Map.Entry<Integer, Integer> entry = iterator.next();

			// Check if this key is the required key
			if (cellFirst == entry.getKey() || cellFirst==entry.getValue()) {
				isKeyPresent = true;

			}
		}

		if (isKeyPresent){
			System.out.println("Value Present");
			return ladderFirstCell();
		} else {
			return cellFirst;
		}

	}
	public int ladderSecondCell(int cellFirst){
		Random ran = new Random();

		int cellSecond = cellFirst+20 + ran.nextInt(79-cellFirst);

		if (cellFirst>cellSecond){
			return ladderSecondCell(cellFirst);
		}
		Iterator<Map.Entry<Integer, Integer> >
				iterator = ladderMap.entrySet().iterator();

		boolean isKeyPresent = false;

		// Iterate over the HashMap
		while (iterator.hasNext()) {

			// Get the entry at this iteration
			Map.Entry<Integer, Integer>
					entry
					= iterator.next();

			// Check if this key is the required key
			if (cellSecond == entry.getKey() || cellSecond==entry.getValue()) {
				isKeyPresent = true;

			}
		}

		if (isKeyPresent){
			System.out.println("Value Present");
			return ladderFirstCell();
		} else {
			return cellSecond;
		}

	}

	private Array<Table> createLadder(int laddersNumber) {
		Array <Table> a= new Array<>(laddersNumber);

		for(int i=0;i<laddersNumber;i++) {

			Table ladder = new Table();
			ladder.setOrigin(Align.center);
			ladder.setTransform(true);
			int cellFirst = ladderFirstCell();
			System.out.println(cellFirst);
			int cellSecond = ladderSecondCell(cellFirst);

			if (cellSecond>cellFirst){
				cellFirst = ladderFirstCell();
				cellSecond = ladderSecondCell(cellFirst);
			}

			ladderMap.put(cellFirst, cellSecond);

			System.out.println((cellFirst+1)+"  -->"+ (cellSecond+1));

			System.out.println(ladderMap.size());

			ladder.add(drawLadder(cellFirst,cellSecond));

			Vector2 ladderTopCell = new Vector2((group.table[cellSecond].getX()+group.table[cellSecond].getWidth()/2),(group.table[cellSecond].getY()+group.table[cellSecond].getHeight()/2));
			Vector2 ladderMid=getMidPoint(group.table[cellFirst].getX()+group.table[cellFirst].getWidth()/2,group.table[cellFirst].getY()+group.table[cellFirst].getHeight()/2,ladderTopCell.x,ladderTopCell.y);

			ladder.setPosition(ladderMid.x,ladderMid.y);

			double theta = Math.atan2(ladderTopCell.y -ladderMid.y, ladderTopCell.x - ladderMid.x);
			theta += Math.PI/2.0;
			double angle = Math.toDegrees(theta);//radian to degree
			if (angle < 0) {
				angle += 360;
			}
			ladder.rotateBy((float) angle);

			System.out.println(" angle "+angle);
			a.add(ladder);
		}
		return a;
	}

	public Table drawLadder(int cellFirst,int cellSecond){
		Table ladder= new Table();
		ladder.setOrigin(Align.center);
		Image head= new Image(new Texture("Stairs-top.png"));
		Table headTable= new Table();
		headTable.add(head).width(group.table[0].getWidth()*0.8f).height(group.table[0].getHeight()*0.5f);
		ladder.add(headTable).row();


		double height=calculateDistanceBetweenPoints(group.table[cellFirst].getX()+group.table[cellFirst].getWidth()/2,group.table[cellFirst].getY()+group.table[cellFirst].getHeight()/2,group.table[cellSecond].getX()+group.table[cellSecond].getWidth()/2,group.table[cellSecond].getY()+group.table[cellSecond].getHeight()/2);
		System.out.println("Ladder height-->"+height);

		height = height-(2*(group.table[0].getHeight()*0.5f));
		if(height<1){
			height=group.table[0].getHeight()*0.8f;
		}

		long NumOfMidRequired= Math.round(height/(group.table[0].getHeight()*0.8f));
		for (int i= 0;i<NumOfMidRequired;i++){
			Image medial = new Image(new Texture("Stairs.png"));
			Table medialTable= new Table();
			medialTable.add(medial).width(group.table[0].getWidth()*0.8f).height(group.table[0].getHeight()*0.8f);
			ladder.add(medialTable).expandY().fillY().row();

		}


		TextureRegion textureRegion = new TextureRegion(new Texture("Stairs-top.png"));
		textureRegion.flip(false, true);

		Image bottom = new Image(textureRegion);
		Table bottomTable= new Table();
		bottomTable.add(bottom).width(group.table[0].getWidth()*0.8f).height(group.table[0].getHeight()*0.5f);
		ladder.add(bottomTable).expandY().fillY();

		return ladder;
	}


	public int snakeBiteCell(int tail){
		Random ran = new Random();

		int biteCell = tail +10 + ran.nextInt(89-tail);

		if (biteCell<tail){
			return snakeBiteCell(tail);
		}

		Iterator<Map.Entry<Integer, Integer> >
				iterator = ladderMap.entrySet().iterator();

		boolean isKeyPresent = false;

		// Iterate over the HashMap
		while (iterator.hasNext()) {

			// Get the entry at this iteration
			Map.Entry<Integer, Integer>
					entry
					= iterator.next();

			// Check if this key is the required key
			if (biteCell == entry.getKey() || biteCell==entry.getValue()) {
				isKeyPresent = true;

			}
		}

		if (isKeyPresent){
			System.out.println("Value Present");
			return ladderFirstCell();
		} else {
			return biteCell;
		}

	}

	private Array<Table> createSnake(int laddersNumber) {
		Array <Table> a= new Array<>(laddersNumber);

		for(int i=0;i<laddersNumber;i++) {
			Table ladder = new Table();
			ladder.setOrigin(Align.center);
			ladder.setTransform(true);


			int tail = ladderFirstCell();
			System.out.println("tail-->"+tail);

			int faceCell = snakeBiteCell(tail);

			if (tail>faceCell){
				tail = ladderFirstCell();
				faceCell = snakeBiteCell(tail);
			}

			ladderMap.put(faceCell ,tail);


			System.out.println((faceCell+1)+"  -->"+ (tail+1));

			System.out.println(ladderMap.size());

			ladder.add(drawSnake(faceCell,tail));

			Vector2 ladderTopCell = new Vector2((group.table[tail].getX()+group.table[tail].getWidth()/2),(group.table[tail].getY()+group.table[tail].getHeight()/2));
			Vector2 ladderMid=getMidPoint(group.table[faceCell].getX()+group.table[faceCell].getWidth()/2,group.table[faceCell].getY()+group.table[faceCell].getHeight()/2,ladderTopCell.x,ladderTopCell.y);

			ladder.setPosition(ladderMid.x,ladderMid.y);

			double theta = Math.atan2(ladderTopCell.y -ladderMid.y, ladderTopCell.x - ladderMid.x);
			theta += Math.PI/2.0;
			double angle = Math.toDegrees(theta);//radian to degree
			if (angle < 0) {
				angle += 360;
			}
			ladder.rotateBy((float) angle);

			System.out.println(" angle "+angle);
			a.add(ladder);
		}
		return a;
	}

	public Table drawSnake(int cellFirst,int cellSecond){
		float r= (float) Math.random();
		float g= (float) Math.random();
		float b= (float) Math.random();
		Table ladder= new Table();
		ladder.setOrigin(Align.center);
		Image face= new Image(new Texture("Snake-Face.png"));
		Table headTable= new Table();
		face.setColor(r,g,b,1);
		headTable.add(face).width(group.table[0].getWidth()*0.4f).height(group.table[0].getHeight()*0.5f);
		ladder.add(headTable).row();


		double height=calculateDistanceBetweenPoints(group.table[cellFirst].getX()+group.table[cellFirst].getWidth()/2,group.table[cellFirst].getY()+group.table[cellFirst].getHeight()/2,group.table[cellSecond].getX()+group.table[cellSecond].getWidth()/2,group.table[cellSecond].getY()+group.table[cellSecond].getHeight()/2);
		System.out.println("Ladder height-->"+height);

		height = height-(2*(group.table[0].getHeight()*0.5f));
		if(height<1){
			height=group.table[0].getHeight();
		}

		long NumOfMidRequired= Math.round(height/(group.table[0].getHeight()));
		for (int i= 0;i<NumOfMidRequired;i++){
			Image medial = new Image(new Texture("Snake-Body.png"));
			Table medialTable= new Table();
//			medial.rotateBy(12);
			medial.setColor(r,g,b,1);

			medialTable.add(medial).width(group.table[0].getWidth()*0.4f).height(group.table[0].getHeight());
			ladder.add(medialTable).expandY().fillY().row();

		}


		TextureRegion textureRegion = new TextureRegion(new Texture("Snake-Tail.png"));

		Image tail = new Image(textureRegion);
		Table bottomTable= new Table();
		tail.setColor(r,g,b,1);

		bottomTable.add(tail).width(group.table[0].getWidth()*0.4f).height(group.table[0].getHeight()*0.5f);
		ladder.add(bottomTable).expandY().fillY();

		return ladder;
	}

	private Vector2 getMidPoint(float x1, float y1, float x2, float y2){
		Vector2 vector2= new Vector2();
		vector2.x= (x1+x2)/2;
		vector2.y=(y1+y2)/2;
		return vector2;
	}
	
	private double  calculateDistanceBetweenPoints(double  x1,double y1, double x2, double y2){
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();


	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
