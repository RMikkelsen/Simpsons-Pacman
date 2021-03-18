package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener {

    //used for pacman movements
    private var game: Game? = null
    private var myTimer: Timer = Timer()
    var counter: Int = 0

    //constants for directions - define the rest yourself
    //for game timer (60 sec)
    private var Timer2: Timer = Timer()
    var counter2: Int = 26
    //reference to the game class.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        play.setOnClickListener(this)
        pause.setOnClickListener(this)
        reset.setOnClickListener(this)


        game?.running = true //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }, 0, 200) //0 indicates we start now, 200
        //is the number of miliseconds between each call

        //this timer for countdown
        Timer2.schedule(object : TimerTask() {
            override fun run() {
                timerMethod2()
            }

        }, 0, 1000) //0 indicates we start now, 1000 is for each second

        game = Game(this, pointsView)
        //intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveRight.setOnClickListener {
            game?.direction = 0
        }
        moveLeft.setOnClickListener {
            game?.direction = 1

        }
        moveUp.setOnClickListener {
            game?.direction = 2

        }
        moveDown.setOnClickListener {
            game?.direction = 3

        }
    }

    override fun onStop() {
        super.onStop()
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel()
    }

    private fun timerMethod() {
        //This method is called directly by the timer and runs in the same thread as the timer.
        //We call the method that will work with the UI through the runOnUiThread method.
        this.runOnUiThread(timerTick)

    }

    private fun timerMethod2() {
        this.runOnUiThread(timerTick2)
    }

    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game?.running == true) {
            counter++
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            textView.text = getString(R.string.timerValue, counter)


            if (game?.direction == 0) { // move right
                game?.movePacmanRight(20)
                game?.moveEnemyRight(10)
                //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this - you have already made that
            } else if (game?.direction == 1) {
                game?.movePacmanLeft(20)
                game?.moveEnemyLeft(10)
            } else if (game?.direction == 2) {
                game?.movePacmanUp(20)
                game?.moveEnemyUp(10)
            } else if (game?.direction == 3) {
                game?.movePacmanDown(20)
                game?.moveEnemyDown(10)
            }
        }
    }

    //if anything is pressed - we do the checks here
    override fun onClick(v: View) {
        if (v.id == R.id.play) {
            game?.running = true
        } else if (v.id == R.id.pause) {
            game?.running = false
        } else if (v.id == R.id.reset) {
            counter = 0
            counter2 = 26
            game?.newGame() //you should call the newGame method instead of this
            game?.running = false
            textView.text = getString(R.string.timerValue, counter)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.running = false
            counter = 0
            counter2 = 26
            game?.newGame()
            textView.text = getString(R.string.timerValue, counter)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // this is for 2nd timer
    private val timerTick2 = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game?.running == true) {
            counter2--
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            textView3.text = getString(R.string.timerValue2, counter2)
            if (counter2 <= 0) {
                Toast.makeText(this, "GAME OVER!!", Toast.LENGTH_SHORT).show()
                game?.running = false
                game?.newGame()
                counter2 = 26

            }
        }

    }
}
