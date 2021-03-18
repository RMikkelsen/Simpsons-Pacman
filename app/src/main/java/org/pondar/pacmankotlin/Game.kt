package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import java.util.ArrayList
import android.util.Log
import android.widget.Toast
import java.util.*

/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView) {

    private var pointsView: TextView = view
    private var points: Int = 0

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var pacx = 0
    var pacy = 0
    var coinBitmap: Bitmap
    var enemyBitmap: Bitmap
    var enemyx = 0
    var enemyy = 0
    var running = false
    var direction = 0


    //did we initialize the coins?
    var coinsInitialized = false
    var enemyInitialized = false

    //the list
    var coins = ArrayList<GoldCoin>()
    var enemy = ArrayList<Enemy>()

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h = 0
    private var w = 0 //height and width of screen

    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.burns)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {
        //DO Stuff to initialize with some coins.
        //DO Stuff to initialize the array list with some coins.
        var minX: Int = 0
        var maxX: Int = w - coinBitmap.width
        var minY: Int = 0
        var maxY: Int = h - coinBitmap.width
        val random = Random()
        for (i in 0..8) {
            var randomX: Int = random.nextInt(maxX - minX + 1) + minX
            var randomY: Int = random.nextInt(maxY - minY + 1) + minY
            coins.add(GoldCoin(randomX, randomY, false))
        }
        coinsInitialized = true
    }

    //TODO initialize enemy also here
    fun initializeenemy() {
        //DO Stuff to initialize with some enemy
        //DO Stuff to initialize the array list with enemy
        var minX: Int = 0
        var maxX: Int = w - enemyBitmap.width
        var minY: Int = 0
        var maxY: Int = h - enemyBitmap.width
        val random = Random()
        for (i in 0..3) {
            var randomX: Int = random.nextInt(maxX - minX + 1) + minX
            var randomY: Int = random.nextInt(maxY - minY + 1) + minY
            enemy.add(Enemy(randomX, randomY, false))
        }
        enemyInitialized = true

    }


    fun newGame() {
        pacx = 100
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        coins.clear()
        coinsInitialized = false
        enemy.clear()
        enemyInitialized = false

        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }


    //works check sides/boundaries
    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }


    //works check sides/boundaries
    fun moveEnemyRight(pixels: Int) {


        for (enemy in enemy) {

            if (enemy.enemyx + pixels + enemyBitmap.width < w) {
                enemy.enemyx = enemyx + pixels
                doCollisionCheck2()
                gameView!!.invalidate()
            }
        }
    }

    //works
    fun moveEnemyLeft(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyx > 0) {
                enemy.enemyx = enemyx - pixels
                doCollisionCheck2()
                gameView!!.invalidate()
            }
        }
    }

    //works
    fun moveEnemyUp(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyy > 0) {
                enemy.enemyy = enemyy - pixels
                doCollisionCheck2()
                gameView!!.invalidate()
            }
        }
    }

    //works
    fun moveEnemyDown(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyx + pixels + enemyBitmap.height < h) {
                enemy.enemyy = enemyy + pixels
                doCollisionCheck2()
                gameView!!.invalidate()
            }
        }
    }

    //TODO check if the pacman touches an enemy
    fun doCollisionCheck2() {
        for (enemy in enemy) {
            if (pacx + pacBitmap.width >= enemy.enemyx && pacx <= enemy.enemyx + enemyBitmap.width && pacy + pacBitmap.height >= enemy.enemyy && pacy <= enemy.enemyy + enemyBitmap.height && !enemy.alive) {
                Toast.makeText(this.context, "DOH!!", Toast.LENGTH_SHORT).show()
                enemy.alive = false
                //return newGame()
            }
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        for (coin in coins) {
            if (pacx + pacBitmap.width >= coin.coinX && pacx <= coin.coinX + coinBitmap.width && pacy + pacBitmap.height >= coin.coinY && pacy <= coin.coinY + coinBitmap.height && !coin.taken) {
                Toast.makeText(this.context, "Woo Hoo!!", Toast.LENGTH_SHORT).show()
                coin.taken = true
                points += 1
                pointsView.text = "${context.resources.getString(R.string.points)}$points"
            }
            if (points == coins.size && coin.taken) {
                return newGame()
            }
        }

    }
}




