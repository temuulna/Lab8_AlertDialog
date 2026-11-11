package com.example.lab8_alertdialog


import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    lateinit var btnPizzaObj: Button
    lateinit var btnToppingObj: Button
    lateinit var btnOrderObj: Button
    lateinit var ivBgObj: ImageView
    lateinit var ivPizzaObj: ImageView
    lateinit var tvPizzaObj: TextView
    lateinit var tvToppingObj: TextView
    lateinit var ivDeliveryObj: ImageView
    lateinit var tvDeliveryObj: TextView

    // 피자 / 토핑 그림 배열
    var drawablePizza = intArrayOf(R.drawable.pizza1, R.drawable.pizza2, R.drawable.pizza3, R.drawable.pizza4)
    var drawableTopping = intArrayOf(R.drawable.topping1, R.drawable.topping2, R.drawable.topping3,
        R.drawable.topping4, R.drawable.topping5)
    var imageViewTopping = intArrayOf(R.id.topping1, R.id.topping2, R.id.topping3, R.id.topping4, R.id.topping5)
    var ivToppingObj = arrayOfNulls<ImageView>(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnPizzaObj = findViewById(R.id.button_pizza)
        btnToppingObj = findViewById(R.id.button_toppting)
        btnOrderObj = findViewById(R.id.button_order)
        ivBgObj = findViewById(R.id.imageView_bg)
        ivPizzaObj = findViewById(R.id.pizza)
        tvPizzaObj = findViewById(R.id.textView_pizza)
        tvToppingObj = findViewById(R.id.textView_toppting)
        ivDeliveryObj = findViewById(R.id.delivery)
        tvDeliveryObj = findViewById(R.id.textView_delivery)
        for (i in 0..4) ivToppingObj[i] = findViewById(imageViewTopping[i])

        // 피자를 고르기 전에는 토핑/주문 버튼 비활성화
        btnToppingObj.isEnabled = false
        btnOrderObj.isEnabled = false

        var selectNum = 0   // 대화상자에서 지금 고른 피자
        var oldNum = 0      // 마지막으로 확정한 피자

        // ① 피자종류: 하나만 고르는 목록 대화상자
        btnPizzaObj.setOnClickListener {
            val listItems = arrayOf("마르게리타 피자", "불고기 피자", "양송이 피자", "하와이안 피자")
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.icon_pizza)
            alertDialog.setTitle("주문할 피자를 선택하세요")

            // 목록에서 항목을 누를 때
            val itemListener = DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, listItems[which], Toast.LENGTH_SHORT).show()
                selectNum = which
            }
            // 예 / 아니오 버튼
            val btnListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        ivBgObj.isVisible = false
                        ivDeliveryObj.isVisible = false
                        tvDeliveryObj.isVisible = false
                        btnToppingObj.isEnabled = true
                        btnOrderObj.isEnabled = true
                        ivPizzaObj.isVisible = true
                        tvPizzaObj.isVisible = true
                        ivPizzaObj.setImageResource(drawablePizza[selectNum])
                        tvPizzaObj.text = listItems[selectNum]
                        oldNum = selectNum
                        tvToppingObj.text = ""
                        for (i in 0..4) ivToppingObj[i]?.isVisible = false   // 피자 바꾸면 토핑 초기화
                    }
                    DialogInterface.BUTTON_NEGATIVE -> { }
                }
                dialog.dismiss()
            }

            alertDialog.setSingleChoiceItems(listItems, oldNum, itemListener)
            alertDialog.setPositiveButton("예", btnListener)
            alertDialog.setNegativeButton("아니오", btnListener)
            alertDialog.show()
        }

        // ② 피자토핑: 여러 개 고르는 목록 대화상자
        btnToppingObj.setOnClickListener {
            val listItems = arrayOf("양송이", "불고기", "바질", "베이컨", "해물")
            val checkedArray = booleanArrayOf(false, false, false, false, false)
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.icon_pizza)
            alertDialog.setTitle("추가할 토핑을 선택하세요")

            // 체크박스를 누를 때마다 체크 상태 저장
            val itemListener = DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                Toast.makeText(this, listItems[which], Toast.LENGTH_SHORT).show()
                checkedArray[which] = isChecked
            }
            val btnListener = DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    tvToppingObj.isVisible = true
                    var str = "토핑추가 : "
                    for (i in 0..4) {
                        ivToppingObj[i]?.isVisible = false
                        if (checkedArray[i]) {
                            ivToppingObj[i]?.isVisible = true
                            ivToppingObj[i]?.setImageResource(drawableTopping[i])
                            str += listItems[i] + " "
                        }
                    }
                    tvToppingObj.text = str
                }
                dialog.dismiss()
            }
            alertDialog.setMultiChoiceItems(listItems, checkedArray, itemListener)
            alertDialog.setPositiveButton("예", btnListener)
            alertDialog.setNegativeButton("아니오", btnListener)
            alertDialog.show()
        }

        // ③ 주문완료: 예/아니오 확인 대화상자
        btnOrderObj.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.icon_pizza)
            alertDialog.setMessage("주문하겠습니까?")
            val listener = DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    ivDeliveryObj.isVisible = true
                    tvDeliveryObj.isVisible = true
                }
                dialog.dismiss()
            }
            alertDialog.setPositiveButton("예", listener)
            alertDialog.setNegativeButton("아니오", listener)
            alertDialog.show()
        }
    }
}