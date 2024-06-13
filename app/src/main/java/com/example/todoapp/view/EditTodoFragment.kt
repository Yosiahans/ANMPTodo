package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCreateTodoBinding
import com.example.todoapp.databinding.FragmentEditTodoBinding
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.DetailTodoViewModel


class EditTodoFragment : Fragment(),RadioClickListener,TodoEditClickListener {
    private lateinit var binding: FragmentEditTodoBinding
    private lateinit var viewModel:DetailTodoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)
        binding.radioListener = this

        observeViewModel()



//        binding.btnAdd.setOnClickListener{
//            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            viewModel.update(binding.txtTitle.text.toString(),binding.txtNotes.text.toString(),radio.tag.toString().toInt(),uuid)
//            Toast.makeText(view.context, "Todo updated", Toast.LENGTH_SHORT).show()
//            Navigation.findNavController(it).popBackStack()
//
//        }
    }

    fun observeViewModel(){
        viewModel.todoLD.observe(viewLifecycleOwner,Observer{
            binding.todo= it

            when(it.priority){
                1 -> binding.radioLow.isChecked=true
                2 -> binding.radioMedium.isChecked=true
                else -> binding.radioHigh.isChecked=true
            }
        })
    }

//    override fun onRadioClick(v: View, priority: Int, todo: Todo) {
//        todo.priority = priority
//    }

    override fun onRadioClick(v: View) {
       binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun onTodoEditClick(v: View) {
        viewModel.update1(binding.todo!!)
    }


}