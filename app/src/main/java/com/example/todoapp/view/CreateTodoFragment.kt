package com.example.todoapp.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

import com.example.todoapp.R
import com.example.todoapp.TodoWorker
import com.example.todoapp.databinding.FragmentCreateTodoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.util.NotificationHelper
import com.example.todoapp.viewmodel.DetailTodoViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit


class CreateTodoFragment : Fragment(),RadioClickListener,ButtonAddTodoClickListener,DateClickListener,TimeClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {


    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var dataBinding: FragmentCreateTodoBinding

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_todo,
            container,false)
        dataBinding.todo = Todo("","",3,0,0)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
//        dataBinding.todo = this
//        dataBinding.listener = this
        return binding.root
        dataBinding.listener = this
        dataBinding.listenerDate = this
        dataBinding.listenerTime = this
        dataBinding.radiolistener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),NotificationHelper.REQUEST_NOTIF)
        }

        viewModel =
            ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.btnAdd.setOnClickListener{
//            val list = listOf(dataBinding.todo!!)
//            viewModel.addTodo(list)
//            Toast.makeText(view.context,"Data Added", Toast.LENGTH_LONG).show()
            val workRequest = OneTimeWorkRequestBuilder<TodoWorker>().setInitialDelay(30, TimeUnit.SECONDS).setInputData(
                workDataOf("Title" to "TODO created", "message" to "Stay Focus")).build()
            WorkManager.getInstance(requireContext()).enqueue(workRequest)

            var radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)

            var todo=Todo(binding.txtTitle.text.toString(),binding.txtNotes.text.toString(),radio.tag.toString().toInt(),0,0)
            val list = listOf(dataBinding.todo!!)
            viewModel.addTodo(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()

            val notif = NotificationHelper(view.context)
            notif.createNotification("Todo Created","A New Todo has been created! Stay Focus!")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == NotificationHelper.REQUEST_NOTIF){
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                NotificationHelper(requireContext()).createNotification("Todo Created","A new todo has been created")
            }
        }
    }

    override fun onButtonAddTodo(v: View) {
        val c = Calendar.getInstance()
        c.set(year,month,day,hour,minute,0)
        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)

        dataBinding.todo!!.todo_Date = (c.timeInMillis/1000L).toInt()

        val list = listOf(dataBinding.todo!!)
        viewModel.addTodo(list)
        Toast.makeText(v.context,"Data Added",Toast.LENGTH_LONG).show()
        val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>().setInitialDelay(diff,TimeUnit.SECONDS).setInputData(
            workDataOf("title" to "Todo Created","message" to "A New Todo has been Created! Stay Focus")).build()
        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        TODO("Not yet implemented")
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        activity?.let{
            it1 -> DatePickerDialog(it1, this,year,month,day).show()
        }
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(),this,hour,minute,true).show()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().let{
            it.set(year,month,day)
            dataBinding.txtDate.setText(day.toString().padStart(2,'0')+"-"+month.toString().padStart(2,'0')+'-'+year)
            this.year=year
            this.month = month
            this.day = day
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.txtTime.setText(
            hour.toString().padStart(2,'0') + ":" +
                    minute.toString().padStart(2,'0')
        )
        this.hour = hour
        this.minute = minute

    }
}