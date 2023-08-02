package Database.Firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager<T>(
    private val collectionName: String,
    private val clazz: Class<T>,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collectionReference: CollectionReference
        get() = firestore.collection(collectionName)

    // ...

    fun getAllItems(callback: (List<T>?, Exception?) -> Unit) {
        collectionReference.get()
            .addOnSuccessListener { querySnapshot ->
                val itemList = mutableListOf<T>()
                for (document in querySnapshot.documents) {
                    val item = document.toObject(clazz)
                    if (item != null) {
                        itemList.add(item)
                    }
                }
                callback(itemList, null)
            }
            .addOnFailureListener { exception ->
                callback(null, exception)
            }
    }
}