package com.example.acrominerestservice_adamwisecarver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationItem
import com.example.acrominerestservice_adamwisecarver.databinding.ActivityMainBinding
import com.example.acrominerestservice_adamwisecarver.utils.ApiState
import com.example.acrominerestservice_adamwisecarver.viewmodel.AcromineViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val acromineAdapter = AcromineAdapter(vars = ::showVars)
    private val viewModel: AcromineViewModel by viewModels()
    private val varsDialog by lazy {
        VarsDialogView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            rvAbiList.adapter = acromineAdapter
            btnSearch.setOnClickListener {
                if (etInput.text.isBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter an abbreviation",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.fetchAbbreviation(etInput.text.toString().trim())
                }
            }
        }
        setViewModelObserver()
    }

    private fun setViewModelObserver() {
        viewModel.stateLiveData.observe(this) { state ->
            when (state) {
                is ApiState.Loading -> {
                    binding.apply {
                        pbLoading.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                        rvAbiList.visibility = View.GONE
                    }
                }
                is ApiState.Error -> {
                    binding.apply {
                        pbLoading.visibility = View.GONE
                        rvAbiList.visibility = View.GONE

                        tvError.apply {
                            text = state.exception.message
                            visibility = View.VISIBLE
                        }
                    }
                }
                is ApiState.Success -> {
                    binding.apply {
                        pbLoading.visibility = View.GONE
                        tvError.visibility = View.GONE
                        rvAbiList.apply {
                            acromineAdapter.setNewList(state.response.lfs)
                            visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showVars(vars: List<AbbreviationItem>) {
        varsDialog.buildVarsDialogView(vars)
    }

    override fun onPause() {
        super.onPause()
        varsDialog.dismiss()
    }
}