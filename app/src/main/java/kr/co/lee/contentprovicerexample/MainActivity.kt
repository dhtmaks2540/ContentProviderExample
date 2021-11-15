package kr.co.lee.contentprovicerexample

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    lateinit var listView: ListView
    lateinit var nameView: EditText
    lateinit var phoneView: EditText
    lateinit var btn: Button

    var isUpdate: Boolean = false
    var _id: String? = null

    var datas: ArrayList<HashMap<String, String>>? = null

    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lab1_listview)
        nameView = findViewById(R.id.lab1_name)
        phoneView = findViewById(R.id.lab1_phone)
        btn = findViewById(R.id.lab1_btn)

        // uri 설정
        uri = Uri.parse("content://example_provider")
        setAdapter()

        btn.setOnClickListener {
            insertOrUpdate()
        }
        listView.onItemClickListener = this
        listView.onItemLongClickListener = this
    }

    // 리스트뷰 아이템 클릭 이벤트
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val map: HashMap<String, String>? = datas?.get(position)
        nameView.setText(map?.get("name"))
        phoneView.setText(map?.get("phone"))
        _id = map?.get("id")
        isUpdate = true
    }

    // 리스트뷰 아이템 롱클릭 이벤트
    // 삭제하는 메서드
    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val map: HashMap<String, String>? = datas?.get(position)
        contentResolver.delete(uri, "_id=?", arrayOf(map?.get("id")))
        setAdapter()
        return false
    }

    // 버튼 클릭 메서드
    // 데이터를 넣거나 업데이트하는 메서드
    private fun insertOrUpdate() {
        if(isUpdate) {
            val name = nameView.text.toString()
            val phone = phoneView.text.toString()
            if(name != "" && phone != "") {
                val values = ContentValues().apply {
                    put("name", name)
                    put("phone", phone)
                }
                contentResolver.update(uri, values, "_id=?", arrayOf(_id))
                setAdapter()
            }
            isUpdate = false
        } else {
            val name = nameView.text.toString()
            val phone = phoneView.text.toString()
            if(name != "" && phone != "") {
                val values = ContentValues().apply {
                    put("name", name)
                    put("phone", phone)
                }
                contentResolver.insert(uri, values)
                setAdapter()
            }
        }
        nameView.setText("")
        phoneView.setText("")
    }

    // 항목 구성을 위한 query() 호출
    private fun setAdapter() {
        datas = ArrayList()
        val cursor = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        when(cursor?.count) {
            null -> {
                // 에러 처리 코드
            }
            0 -> {
                // 성공적으로 데이터를 못 받을 경우 처리코드
            }
            else -> {
                while(cursor.moveToNext()) {
                    val map = HashMap<String, String>()
                    map["id"] = cursor.getString(0)
                    map["name"] = cursor.getString(1)
                    map["phone"] = cursor.getString(2)
                    datas?.add(map)
                }
            }
        }

        val adapter = SimpleAdapter(this, datas, android.R.layout.simple_list_item_2,
                    arrayOf("name", "phone"), intArrayOf(android.R.id.text1, android.R.id.text2))
        listView.adapter = adapter
    }
}