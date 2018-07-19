package com.example.mart.eyeonyourhand

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions






class MainActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    val REQUEST_TO_TEXT_RECOGNITION = 2

    /*
    *
    * Declare variable for use in to Text Recognition
    * FirebaseVisionImage
    *
    * */
    lateinit var image: FirebaseVisionImage
    var options = FirebaseVisionLabelDetectorOptions.Builder()
            .setConfidenceThreshold(0.8f)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        takePhoto.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent,CAMERA_REQUEST_CODE)
            }
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            if (intent.resolveActivity(packageManager) != null) {
//                startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null) {
                    var text = ""
//                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
//                    image = FirebaseVisionImage.fromBitmap(data.extras.get("data") as Bitmap)
//                    var detector : FirebaseVisionTextDetector = FirebaseVision.getInstance().visionTextDetector
//                    val result = detector.detectInImage(image)
//                            .addOnSuccessListener(object : OnSuccessListener<FirebaseVisionText> {
//                                override fun onSuccess(p0: FirebaseVisionText?) {
//                                    for (block: FirebaseVisionText.Block in p0!!.blocks) {
//                                        val boundingBox = block.boundingBox!!
//                                        val cornerPoints = block.cornerPoints!!
//                                        text += block.text
//                                        textView.text = text
//
////                                    for (line in block.getLines()) {
////                                        // ...
////                                        for (element in line.getElements()) {
////                                            // ...
////                                        }
////                                    }
//                                    }
//                                }
//                            })
//                            .addOnFailureListener {
//                                Toast.makeText(this,"Unsuccessful",Toast.LENGTH_SHORT).show()
//                            }
                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
                    image = FirebaseVisionImage.fromBitmap(data.extras.get("data") as Bitmap)

                    var detector : FirebaseVisionLabelDetector  = FirebaseVision.getInstance().getVisionLabelDetector(options)
                    val result = detector.detectInImage(image)
                            .addOnSuccessListener { labels ->
                                if (labels != null) {
                                    for (label in labels) {
                                        textView.text = null
                                        textView.append(label.label + "\n")
                                        textView.append(label.confidence.toString())
                                    }
                                }
                            }
                            .addOnFailureListener {
                        Toast.makeText(this,"Unsuccessful",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                Toast.makeText(this,"Unrecognizied request code",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
