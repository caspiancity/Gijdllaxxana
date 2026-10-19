package com.apache.test

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT
    private val GREEN = Color.parseColor("#3FB950")
    private val RED = Color.parseColor("#F85149")
    private val GREY = Color.parseColor("#8B949E")
    private val BLUE = Color.parseColor("#4A8DF0")
    private val CARD = Color.parseColor("#161B22")
    private val BORDER = Color.parseColor("#2A3142")
    private val BG = Color.parseColor("#0D1117")

    private lateinit var root: FrameLayout

    private val servers = mutableListOf("APACHES | YAXIN ZAMANDA", "APACHES | DEV SERVER", "APACHES | 404 NOT FOUND")
    private var server = servers[0]

    private lateinit var loadingCol: LinearLayout
    private lateinit var statusTxt: TextView
    private lateinit var percentTxt: TextView
    private lateinit var progBar: ProgressBar
    private lateinit var pkgBox: LinearLayout
    private var phaseLabel = "DATA YÜKLƏNİR"
    private var isReverse = false
    private var dots = 0

    private var tiltAnim: ValueAnimator? = null
    private var vIndex = 0
    private val videos = listOf("video1", "video2", "video3")

    private val packs = listOf(
        "qara_dildo.pak", "img_təyziqölçən.osw", "nənənin_reseptləri.zip", "kartof_tekstur_v2.dat",
        "balina_mesh_HD.bin", "samp_ruhu.dll", "ağ_qaraciyər.ogg", "bulud_serveri_qiymeti.cfg",
        "mini_pişik_hitbox.col", "dünya_sonu_plugin.so", "tyler1_texture_4K.png", "gecə_qapısı.ipl",
        "fps_patch_ULTRA.bin", "ocaq_sesi_pack.wav", "1000_ping_fix.exe", "yerdəki_qum.dff", "sənin_şəkilin.jpg"
    )

    private val ticker = object : Runnable {
        override fun run() {
            val d = dots++ % 4
            statusTxt.text = phaseLabel + ".".repeat(d)
            if (isReverse) addLine("geri alınır: " + packs.random(), "✗ ", RED)
            else addLine(packs.random(), "✓ ", GREEN)
            if (!isReverse && Random.nextInt(7) == 0) addLine("⚠ xəta 0x7F: ruh qəbul edilmədi", "", RED)
            handler.postDelayed(this, 350)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = FrameLayout(this).apply { setBackgroundColor(BG) }
        setContentView(root)
        showLauncher()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Geri düyməsi bu gün işləmir", Toast.LENGTH_SHORT).show()
    }

    // ---------- helpers ----------

    private fun px(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun bg(color: Int, radius: Int = 0, stroke: Int = 0) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = px(radius).toFloat()
        if (stroke != 0) setStroke(px(1), stroke)
    }

    private fun tv(text: String, size: Float, color: Int = Color.WHITE, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

    private fun View.fadeIn(delay: Long = 0L) {
        alpha = 0f
        translationY = px(24).toFloat()
        animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(delay).start()
    }

    private fun View.shake() {
        ObjectAnimator.ofFloat(this, "translationX", 0f, 24f, -24f, 16f, -16f, 0f).setDuration(400).start()
    }

    private fun showMsg(title: String, msg: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(msg)
            .setPositiveButton("Tamam", null).show()
    }

    private fun card(title: String, value: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(px(20), px(18), px(20), px(18))
        background = bg(CARD, 14, BORDER)
        addView(tv(title, 11f, GREY, true))
        addView(tv(value, 16f).apply { setPadding(0, px(6), 0, 0) })
        layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { bottomMargin = px(14) }
    }

    // ---------- launcher ----------

    private fun showLauncher(prefill: String = "") {
        root.removeAllViews()
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }

        val side = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0F141C"))
            setPadding(px(24), px(40), px(24), px(24))
        }
        side.addView(tv("APACHE ONLINE", 22f, bold = true))
        side.addView(tv("Mobile · v0.3.117", 12f, GREY))

        val menu = listOf(
            "▶  Başla" to "",
            "▭  Data" to "Data limiti: 3,0 GB. Qalan hissə sənin ruhundadır.",
            "⚙  Ayarlar" to "Ayarlar bu gün istirahətdədir.",
            "▦  Alətlər" to "Alətlər səni görmək istəmir.",
            "ⓘ  Haqqında" to "APACHE ONLINE 2019-cu ildən hazırlanır. Hələ bitməyib."
        )
        menu.forEachIndexed { i, (label, msg) ->
            val item = tv(label, 17f, if (i == 0) Color.WHITE else GREY).apply {
                setPadding(px(16), px(14), px(16), px(14))
                background = bg(if (i == 0) Color.parseColor("#1B2638") else Color.TRANSPARENT, 12)
                setOnClickListener { if (msg.isNotEmpty()) showMsg(label, msg) }
            }
            side.addView(item, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = px(10) })
        }
        side.addView(View(this), LinearLayout.LayoutParams(MATCH, 0, 1f))
        side.addView(
            tv("● Onlayn", 14f).apply {
                setPadding(px(16), px(12), px(16), px(12))
                background = bg(Color.parseColor("#262C36"), 12)
            },
            LinearLayout.LayoutParams(MATCH, WRAP)
        )

        val left = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(px(40), 0, px(40), 0)
            gravity = Gravity.CENTER_VERTICAL
        }
        left.addView(tv("Xoş gəldin", 32f, bold = true))
        left.addView(tv("Sadəcə ad qoy və başla!", 16f, GREY))
        left.addView(tv("OYUNÇU ADI", 11f, GREY, true).apply { setPadding(0, px(28), 0, px(6)) })

        val nameInput = EditText(this).apply {
            hint = "Adını yaz"
            setHintTextColor(GREY)
            setTextColor(Color.WHITE)
            maxLines = 1
            inputType = InputType.TYPE_CLASS_TEXT
            background = bg(Color.parseColor("#1E2430"), 12, BORDER)
            setPadding(px(20), px(16), px(20), px(16))
            setText(prefill)
        }
        left.addView(nameInput, LinearLayout.LayoutParams(MATCH, WRAP))

        val status = tv("", 13f, GREEN).apply { setPadding(px(6), px(6), 0, 0) }
        left.addView(status)
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val t = s?.toString()?.trim().orEmpty()
                when {
                    t.isEmpty() -> status.text = ""
                    t.equals("test", true) -> { status.text = "Test? Ciddi?"; status.setTextColor(RED) }
                    t.length < 3 -> { status.text = "Ad çox qısadır"; status.setTextColor(RED) }
                    else -> { status.text = "Ad uyğundur"; status.setTextColor(GREEN) }
                }
            }
        })

        val serverRow = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(px(20), px(14), px(20), px(14))
            background = bg(Color.parseColor("#1E2430"), 12, BORDER)
            addView(tv("SERVER", 11f, GREY, true))
            addView(tv(server, 15f).apply { setPadding(0, px(4), 0, 0) })
            setOnClickListener { pickServer(nameInput.text.toString()) }
        }
        left.addView(serverRow, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = px(18) })
        left.addView(tv("+ Server əlavə et", 13f, BLUE).apply {
            setPadding(px(6), px(8), 0, 0)
            setOnClickListener { addServer(nameInput.text.toString()) }
        })

        val btn = tv("BAŞLA", 20f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
            letterSpacing = 0.15f
            background = bg(BLUE, 14)
            setPadding(0, px(18), 0, px(18))
            setOnClickListener {
                val name = nameInput.text.toString().trim()
                if (name.length < 3) {
                    nameInput.shake()
                    status.text = "Əvvəl ad yaz"
                    status.setTextColor(RED)
                } else {
                    startLoading(name)
                }
            }
        }
        left.addView(btn, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = px(24) })
        ObjectAnimator.ofFloat(btn, "alpha", 0.85f, 1f).apply {
            duration = 1200
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        val right = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(px(20), 0, px(40), 0)
        }
        right.addView(card("DATA", "● Onlayn · 3,0 GB"))
        right.addView(card("QOŞULMA", server))
        right.addView(card("PERFORMANS", "FPS limit: 120  ·  Şrift: 39"))

        row.addView(side, LinearLayout.LayoutParams(px(260), MATCH))
        row.addView(left, LinearLayout.LayoutParams(0, MATCH, 1.3f))
        row.addView(right, LinearLayout.LayoutParams(0, MATCH, 1f))
        root.addView(row, FrameLayout.LayoutParams(MATCH, MATCH))

        left.fadeIn(100)
        right.fadeIn(300)
    }

    private fun pickServer(name: String) {
        AlertDialog.Builder(this).setTitle("Server seç")
            .setItems(servers.toTypedArray()) { _, i ->
                server = servers[i]
                showLauncher(name)
            }
            .show()
    }

    private fun addServer(name: String) {
        val input = EditText(this).apply { hint = "Server adı" }
        AlertDialog.Builder(this).setTitle("Server əlavə et")
            .setView(input)
            .setPositiveButton("Əlavə et") { _, _ ->
                val s = input.text.toString().trim()
                if (s.isNotEmpty()) {
                    servers.add(s)
                    server = s
                }
                showLauncher(name)
                showMsg("Server əlavə edildi", "Qoşulmaq üçün 1 saat gözləmək lazımdır.")
            }
            .setNegativeButton("Ləğv et", null)
            .show()
    }

    // ---------- fake loading ----------

    private fun startLoading(name: String) {
        handler.removeCallbacks(ticker)
        root.removeAllViews()
        root.setBackgroundColor(BG)

        loadingCol = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(px(60), px(40), px(60), px(40))
        }
        loadingCol.addView(tv("Salam, $name. Serverə qoşulur...", 14f, GREY))

        statusTxt = tv("DATA YÜKLƏNİR", 28f, Color.WHITE, true).apply { setPadding(0, px(14), 0, 0) }
        percentTxt = tv("0%", 16f, BLUE).apply { setPadding(0, px(6), 0, 0) }
        progBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 1000
            progress = 0
        }
        pkgBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, px(24), 0, 0)
        }

        loadingCol.addView(statusTxt)
        loadingCol.addView(percentTxt)
        loadingCol.addView(progBar, LinearLayout.LayoutParams(MATCH, px(14)).apply { topMargin = px(14) })
        loadingCol.addView(pkgBox, LinearLayout.LayoutParams(MATCH, WRAP))
        root.addView(loadingCol, FrameLayout.LayoutParams(MATCH, MATCH))
        loadingCol.fadeIn()

        phaseLabel = "DATA YÜKLƏNİR"
        isReverse = false
        dots = 0
        handler.postDelayed(ticker, 300)
        runUp()
    }

    private fun addLine(text: String, prefix: String, color: Int) {
        val line = tv(prefix + text, 13f, color).apply { typeface = Typeface.MONOSPACE }
        pkgBox.addView(line)
        if (pkgBox.childCount > 6) pkgBox.removeViewAt(0)
    }

    private fun updateBar(v: Int) {
        progBar.progress = v
        percentTxt.text = "${v / 10}%"
    }

    private fun runUp() {
        ValueAnimator.ofInt(0, 720).apply {
            duration = 4500
            interpolator = LinearInterpolator()
            addUpdateListener { updateBar(it.animatedValue as Int) }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) {
                    phaseLabel = "Serverdən cavab gözlənilir"
                    handler.postDelayed({ runDown() }, 1500)
                }
            })
            start()
        }
    }

    private fun runDown() {
        isReverse = true
        phaseLabel = "Yükləmə geri qaytarılır"
        ValueAnimator.ofInt(720, 0).apply {
            duration = 4000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { updateBar(it.animatedValue as Int) }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) {
                    handler.postDelayed({ crashSequence() }, 700)
                }
            })
            start()
        }
    }

    private fun crashSequence() {
        handler.removeCallbacks(ticker)
        val w = root.width.toFloat()
        val h = root.height.toFloat()
        val pts = listOf(
            -w * 0.4f to -h * 0.4f,
            w * 0.4f to -h * 0.4f,
            w * 0.4f to h * 0.4f,
            -w * 0.4f to h * 0.4f,
            -w * 0.4f to -h * 0.4f
        )
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2600
            interpolator = LinearInterpolator()
            addUpdateListener { a ->
                val t = a.animatedValue as Float
                val s = minOf((t * (pts.size - 1)).toInt(), pts.size - 2)
                val f = t * (pts.size - 1) - s
                val (x0, y0) = pts[s]
                val (x1, y1) = pts[s + 1]
                val bounce = sin(f.toDouble() * Math.PI).toFloat()
                loadingCol.translationX = x0 + (x1 - x0) * f
                loadingCol.translationY = y0 + (y1 - y0) * f - bounce * 80f
                loadingCol.rotation = t * 1440f
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) { fallDown() }
            })
            start()
        }
    }

    private fun fallDown() {
        loadingCol.animate()
            .translationY(root.height * 1.4f)
            .rotation(900f)
            .alpha(0f)
            .setDuration(900)
            .setInterpolator(AccelerateInterpolator(2f))
            .withEndAction { playVideos() }
            .start()
    }

    // ---------- videos ----------

    private fun playVideos() {
        vIndex = 0
        playNextVideo()
    }

    private fun playNextVideo() {
        tiltAnim?.cancel()
        if (vIndex >= videos.size) {
            showFinal()
            return
        }
        val resId = resources.getIdentifier(videos[vIndex], "raw", packageName)
        if (resId == 0) {
            vIndex++
            playNextVideo()
            return
        }

        root.removeAllViews()
        root.setBackgroundColor(Color.BLACK)
        val vv = VideoView(this)
        root.addView(vv, FrameLayout.LayoutParams(MATCH, MATCH, Gravity.CENTER))
        vv.scaleX = 1.25f
        vv.scaleY = 1.25f
        vv.setVideoURI(Uri.parse("android.resource://$packageName/$resId"))
        vv.setOnPreparedListener { mp -> mp.isLooping = false; vv.start() }
        vv.setOnCompletionListener { vIndex++; playNextVideo() }
        vv.setOnErrorListener { _, _, _ -> vIndex++; playNextVideo(); true }

        tiltAnim = ValueAnimator.ofFloat(-8f, 8f).apply {
            duration = 1400
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { vv.rotation = it.animatedValue as Float }
            start()
        }
    }

    private fun showFinal() {
        tiltAnim?.cancel()
        root.removeAllViews()
        root.setBackgroundColor(BG)
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(px(40), px(40), px(40), px(40))
        }
        col.addView(tv("QOŞULMA UĞURSUZ OLDU", 24f, RED, true).apply { gravity = Gravity.CENTER })
        col.addView(
            tv("Serverlə əlaqə kəsildi. Ruhunun bağlantı sürətindən narazıdır.", 15f, GREY).apply {
                gravity = Gravity.CENTER
                setPadding(0, px(12), 0, px(24))
            }
        )
        col.addView(tv("YENİDƏN CƏHD ET", 16f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
            setPadding(px(40), px(16), px(40), px(16))
            background = bg(BLUE, 12)
            setOnClickListener { showLauncher() }
        })
        root.addView(col, FrameLayout.LayoutParams(MATCH, MATCH))
        col.fadeIn()
    }
}