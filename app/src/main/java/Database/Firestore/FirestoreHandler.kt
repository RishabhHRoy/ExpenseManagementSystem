package Database.Firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.KFunction0

class FirestoreHandler() {
    inline fun <reified T> GetAll(
        collection: CollectionReference,
        crossinline callback: (List<T>?, Exception?) -> Unit
    ) {
        collection
            .get()
            .addOnSuccessListener { result ->
                val dataList = ArrayList<T>()
                for (document in result) {
                    val data = document.toObject(T::class.java)
                    dataList.add(data)
                }
                callback(dataList, null)
            }
            .addOnFailureListener { exception ->
                callback(null, exception)
            }
    }

    inline fun <reified T> Read(
        collection: CollectionReference,
        id: String,
        crossinline callback: (T?, Exception?) -> Unit
    ) {
        val docReference = collection.document(id)
        docReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve the data
                    //val snapshot = documentSnapshot.data

                    // If you have a data class representing the document structure, you can map the data to it
                    // For example, assuming you have a data class called "User":
                    // val user = documentSnapshot.toObject(User::class.java)
                    val data = documentSnapshot.toObject(T::class.java)
                    // Handle the data as per your requirements
                    // ...
                    callback(data, null)
                } else {
                    // Document does not exist
                    // Handle the case when the document is not found
                    // ...
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the retrieval
                // ...
                callback(null, exception)
            }
    }

    inline fun <reified T> Create(
        collection: CollectionReference,
        data: MutableMap<String, Any>,
        crossinline callback: (T?, Exception?) -> Unit
    ) {
        collection
            .add(data)
            .addOnSuccessListener {
                // Document added successfully
                callback(null, null)
            }
            .addOnFailureListener { exception ->
                // Failed to add document
                callback(null, exception)
            }
    }

    inline fun <reified T> Update(
        collection: CollectionReference,
        id: String,
        data: MutableMap<String, Any>,
        crossinline callback: (T?, Exception?) -> Unit
    ) {
        val docReference = collection.document(id)
        docReference
            .update(data)
            .addOnSuccessListener {
                // Document added successfully
                callback(null, null)
            }
            .addOnFailureListener { exception ->
                // Failed to add document
                callback(null, exception)
            }
    }

    inline fun <reified T> Delete(collection: CollectionReference,
               id: String,
               crossinline callback: (T?, Exception?) -> Unit) {
        val docReference = collection.document(id)
        docReference
            .delete()
            .addOnSuccessListener {
                // Document added successfully
                callback(null, null)
            }
            .addOnFailureListener { exception ->
                // Failed to add document
                callback(null, exception)
            }
    }
}