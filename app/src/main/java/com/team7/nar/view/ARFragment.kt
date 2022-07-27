package com.team7.nar.view

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.team7.nar.R
import com.team7.nar.databinding.FragmentArBinding

class ARFragment : Fragment() {
    lateinit var binding : FragmentArBinding
    lateinit var session : Session
    var installRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        // Session
        when (ArCoreApk.getInstance().requestInstall(activity, !installRequested)!!) {
            ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                installRequested = true
                Log.d("AR", "AR Core SDK should installed")
            }
            ArCoreApk.InstallStatus.INSTALLED -> {
                Log.d("AR", "AR Core SDK installed")
            }
            else -> {
                Log.d("AR", "Default case")
            }
        }

        session = Session(this.context)
        configureSession(session)




    }

    fun configureSession(session: Session){
        session.configure(session.config)
    }

}