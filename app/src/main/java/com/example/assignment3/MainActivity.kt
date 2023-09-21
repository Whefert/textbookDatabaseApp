package com.example.assignment3

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3.databinding.ActivityMainBinding
import com.example.assignment3.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val textbookDao = (application as TextbookApp).db.textbookDao()
        binding?.btnAdd?.setOnClickListener {
            addRecord(textbookDao)
        }
        lifecycleScope.launch{textbookDao.fetchAllBooks().collect{
            val list = ArrayList(it)
            setupListOfDataIntoRecyclerView(list, textbookDao)
            }
        }
    }

fun addRecord(textbookDao: TextbookDao) {
    val isbn = binding?.etISBN?.text.toString()
    val title = binding?.etTitle?.text.toString()
    val author = binding?.etAuthor?.text.toString()
    val course = binding?.etCourse?.text.toString()

    if (isbn.isNotEmpty() && title.isNotEmpty() && author.isNotEmpty() && course.isNotEmpty()) {
        lifecycleScope.launch {
            textbookDao.insert(
                TextbookEntity(
                    isbn = isbn,
                    title = title,
                    author = author,
                    course = course
                )
            )
            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
            binding?.etISBN?.text?.clear()
            binding?.etTitle?.text?.clear()
            binding?.etAuthor?.text?.clear()
            binding?.etCourse?.text?.clear()
        }
        }else{
            Toast.makeText(applicationContext, "ISBN, Title, Author or Course cannot be blank", Toast.LENGTH_LONG).show()
        }
    }
    private fun setupListOfDataIntoRecyclerView(textBookList: ArrayList<TextbookEntity>, textbookDao: TextbookDao){
        if(textBookList.isNotEmpty()){
            val itemAdapter = ItemAdapter(textBookList,
                {
                    updateId->updateRecordDialogue(updateId,textbookDao)
                },
                {
                    deleteId ->
                    deleteRecordAlertDialog(deleteId, textbookDao)
                }
                )

        binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
        binding?.rvItemsList?.adapter = itemAdapter
        binding?.rvItemsList?.visibility = View.VISIBLE
        binding?.tvNoRecordsAvailable?.visibility = View.GONE
        }else{
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialogue(id:Int, textbookDao: TextbookDao){
        val updateDialog = Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch{textbookDao.fetchBookById(id).collect {
            if(it!=null){
                binding.etUpdateTitle.setText(it.title)
                binding.etUpdateAuthor.setText(it.author)
                binding.etUpdateCourse.setText(it.course)
                binding.etUpdateISBN.setText(it.isbn)
            }

            }
        }

        binding.tvUpdate.setOnClickListener{
            val title = binding.etUpdateTitle.text.toString()
            val author = binding.etUpdateAuthor.text.toString()
            val course = binding.etUpdateCourse.text.toString()
            val isbn = binding.etUpdateISBN.text.toString()
            if(title.isNotEmpty() && author.isNotEmpty() && course.isNotEmpty() && isbn.isNotEmpty()){
                lifecycleScope.launch{
                    textbookDao.update(TextbookEntity(id,isbn, title, author, course))
                    Toast.makeText(applicationContext,
                        "Record Updated",Toast.LENGTH_LONG)
                        .show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext,
                    "Title, author, isbn or course cannot be blank",Toast.LENGTH_LONG)
                    .show()
                updateDialog.dismiss()
            }
        }

        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun deleteRecordAlertDialog(id:Int, textbookDao: TextbookDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
//        builder.setIcon(R.ic)
        builder.setPositiveButton("Yes"){
            dialogInterface, _-> lifecycleScope.launch{
                textbookDao.delete(TextbookEntity(id))
            Toast.makeText(applicationContext,"Record deleted successfully", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){
            dialogInterface, _-> dialogInterface.dismiss() //Dialog will be dismissed
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}



