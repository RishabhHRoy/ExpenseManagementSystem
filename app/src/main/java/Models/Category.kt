package Models

/*
class Category(title: String, description: String, id: String){
    var id: String = ""
    var title: String = ""
    var description: String = ""

    init{
        this.id = id
        this.title = title
        this.description = description
    }

    override fun toString(): String {
        return this.title
    }
}*/

data class Category(val id: String, val title: String, val description: String){
    constructor() : this("","", "")
}
