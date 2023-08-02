package Models

/*
class Expense(title: String, cost: Double, category_id: String, id: String) {
    companion object{
        val NOTE_EDIT_EXTRA = "noteEdit"
    }

    var id: String = ""
    var title: String = ""
    var cost: Double = 0.00
    var category_id: String = ""

    init{
        this.id = id
        this.title = title
        this.cost = cost
        this.category_id = category_id
    }
}*/

data class Expense(val id: String, val title: String, val cost: Double, val categoryId: String)