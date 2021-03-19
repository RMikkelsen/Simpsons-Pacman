package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.widget.TextView
import java.util.ArrayList
import android.widget.Toast
import java.util.*


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView, view2: TextView, view3: TextView) {


    private var pointsView: TextView = view
    private var points: Int = 0

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var pacx = 0
    var pacy = 0
    var coinBitmap: Bitmap
    var enemyBitmap: Bitmap
    var running = false
    var direction = 0
    var counter: Int = 0
    var counter2: Int = 26


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

    var level = 0
    var levelsView: TextView = view2

    var totalPoints = 0
    var totalPointsView: TextView = view3


    fun setGameView(view: GameView) {
        this.gameView = view
    }

    // initialize goldcoins also here
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

    // initialize enemy also here
    fun initializeenemy() {
        //DO Stuff to initialize with some enemy
        //DO Stuff to initialize the array list with enemy
        var minX: Int = 0
        var maxX: Int = w - enemyBitmap.width
        var minY: Int = 0
        var maxY: Int = h - enemyBitmap.width
        val random = Random()
        //changed from range, and collision  check will increase the number of enemies (level +1) to increase level
        for (i in 0..level) {
            var randomX: Int = random.nextInt(maxX - minX + 1) + minX
            var randomY: Int = random.nextInt(maxY - minY + 1) + minY
            enemy.add(Enemy(randomX, randomY, false))
        }
        enemyInitialized = true

    }


    fun newLevel() {
        pacx = 100
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        coins.clear()
        coinsInitialized = false
        enemy.clear()
        enemyInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        levelsView.text = "${"Level:"} ${level + 1}"
        totalPointsView.text = "${"Total Points:"} $totalPoints"
        counter = 0
        counter2 = 26
        running = false
        gameView?.invalidate() //redraw screen
    }

    fun newGame() {
        totalPoints = 0
        level = 0
        newLevel()
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanLeft(pixels: Int) {
        if (pacx > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanUp(pixels: Int) {
        if (pacy > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanDown(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun moveEnemyRight(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyx + pixels + enemyBitmap.width < w) {
                enemy.enemyx = enemy.enemyx + pixels
            }
        }
        doCollisionCheck2()
        gameView!!.invalidate()
    }

    fun moveEnemyLeft(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyx > 0) {
                enemy.enemyx = enemy.enemyx - pixels
            }
        }
        doCollisionCheck2()
        gameView!!.invalidate()
    }

    fun moveEnemyUp(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyy > 0) {
                enemy.enemyy = enemy.enemyy - pixels
            }
        }
        doCollisionCheck2()
        gameView!!.invalidate()
    }

    //works
    fun moveEnemyDown(pixels: Int) {
        for (enemy in enemy) {
            if (enemy.enemyx + pixels + enemyBitmap.height < h) {
                enemy.enemyy = enemy.enemyy + pixels

            }
        }
        doCollisionCheck2()
        gameView!!.invalidate()
    }


    //check if the pacman touches an enemy
    fun doCollisionCheck2() {
        var playerDead = false
        for (enemy in enemy) {
            if (pacx + pacBitmap.width >= enemy.enemyx && pacx <= enemy.enemyx + enemyBitmap.width && pacy + pacBitmap.height >= enemy.enemyy && pacy <= enemy.enemyy + enemyBitmap.height && !enemy.alive) {
                Toast.makeText(this.context, "DOH! GAME OVER!", Toast.LENGTH_SHORT).show()
                playerDead = true
                enemy.alive = false
            }
        }
        if (playerDead == true) {
            return newGame()
        }
    }

    fun doCollisionCheck() {
        for (coin in coins) {
            if (pacx + pacBitmap.width >= coin.coinX && pacx <= coin.coinX + coinBitmap.width && pacy + pacBitmap.height >= coin.coinY && pacy <= coin.coinY + coinBitmap.height && !coin.taken) {
                Toast.makeText(this.context, "Woo Hoo!!", Toast.LENGTH_SHORT).show()
                coin.taken = true

                points += 1
                totalPoints += 1
                pointsView.text = "${context.resources.getString(R.string.points)}$points"
                totalPointsView.text = "${"Total Points:"} $totalPoints"
            }
            if (points == coins.size && coin.taken) {
                // Toast.makeText(this.context, "YOU WON!!", Toast.LENGTH_SHORT).show()
                level += 1
                levelsView.text = "${context.resources.getString(R.string.levels)} ${level}+1"
                return newLevel()
            }
        }

    }
}




