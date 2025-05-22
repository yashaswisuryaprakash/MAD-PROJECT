package com.example.mediaplayersim

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val songTitleTextView: TextView = findViewById(R.id.songTitleTextView)
        val artistNameTextView: TextView = findViewById(R.id.artistNameTextView)
        val btnPrevious: FloatingActionButton = findViewById(R.id.btnPrevious)
        val btnPlay: FloatingActionButton = findViewById(R.id.btnPlay)
        val btnNext: FloatingActionButton = findViewById(R.id.btnNext)
        val btnTeamInfo: ImageView = findViewById(R.id.btnTeamInfo)
        
        // Ensure the play button shows play icon initially
        btnPlay.setImageResource(android.R.drawable.ic_media_play)
        
        // Set song info
        songTitleTextView.text = "ALL_OK_YAAKINGE"
        artistNameTextView.text = "KANNADA_RAP_SONG"
        
        // Initialize MediaPlayer
        initializeMediaPlayer()
        
        // Setup button click listeners
        btnPrevious.setOnClickListener {
            skipBackward()
            Toast.makeText(this, " Backward ", Toast.LENGTH_SHORT).show()
        }
        
        btnPlay.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
                btnPlay.setImageResource(android.R.drawable.ic_media_play)
                Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show()
            } 
            else {
                playAudio()
                btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                Toast.makeText(this, "Playing audio...", Toast.LENGTH_SHORT).show()
            }
            isPlaying = !isPlaying
        }
        
        btnNext.setOnClickListener {
            skipForward()
            Toast.makeText(this, " Forward ", Toast.LENGTH_SHORT).show()
        }
        
        btnTeamInfo.setOnClickListener {
            showTeamInfo()
        }
    }
    
    private fun initializeMediaPlayer() {
        try {
            // You need to add a real MP3 file named sample_audio.mp3 to the raw folder
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_audio)
            mediaPlayer?.setOnCompletionListener {
                // Reset play button when audio completes
                val btnPlay: FloatingActionButton = findViewById(R.id.btnPlay)
                btnPlay.setImageResource(android.R.drawable.ic_media_play)
                isPlaying = false
            }
            
        } catch (e: Exception) {
            // Handle the case where the audio file might not exist
            Toast.makeText(this, "Error loading audio file: ${e.message}", Toast.LENGTH_LONG).show()
            
            // Reset the playing state if there's an error
            isPlaying = false
            val btnPlay: FloatingActionButton = findViewById(R.id.btnPlay)
            btnPlay.setImageResource(android.R.drawable.ic_media_play)
        }
    }
    
    private fun playAudio() {
        mediaPlayer?.start()
    }
    
    private fun pauseAudio() {
        mediaPlayer?.pause()
    }
    
    private fun skipForward() {
        mediaPlayer?.let {
            val currentPosition = it.currentPosition
            val duration = it.duration
            val newPosition = currentPosition + 10000 // Skip forward 10 seconds
            
            if (newPosition < duration) {
                it.seekTo(newPosition)
            } else {
                it.seekTo(duration)
            }
        }
    }
    
    private fun skipBackward() {
        mediaPlayer?.let {
            val currentPosition = it.currentPosition
            val newPosition = currentPosition - 10000 // Skip backward 10 seconds
            
            if (newPosition > 0) {
                it.seekTo(newPosition)
            } else {
                it.seekTo(0)
            }
        }
    }
    
    private fun showTeamInfo() {
        val message = """
            <b><font size="15" color="#D500F9"> Group no: 12 </font></b><br><br>
            <font color="#FFFFFF">22BTDS153 - Adithya N T</font><br>
            <font color="#FFFFFF">22BTDS154 - Shravani S</font><br>
            <font color="#FFFFFF">22BTDS155 - Thanushree N S</font><br>
            <font color="#FFFFFF">22BTDS156 - Yashaswi S</font>
        """.trimIndent()
        
        val builder = AlertDialog.Builder(this, R.style.DarkDialogTheme)
        builder.setTitle("Team Information")
        
        // Handle compatibility for different Android versions
        val formattedMessage = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.text.Html.fromHtml(message, android.text.Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            android.text.Html.fromHtml(message)
        }
        
        builder.setMessage(formattedMessage)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        
        val dialog = builder.create()
        dialog.show()
    }
    
    override fun onDestroy() {
        // Clean up MediaPlayer resources
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
} 