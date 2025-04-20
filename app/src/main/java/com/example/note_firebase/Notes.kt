package com.example.note_firebase

import android.icu.text.CaseMap.Title

public class Notes{
   var id:String? =null
    var title:String? = null
    var content:String? =null
    var time:String?=null

    constructor()
    constructor(id:String?,title:String?,content:String?,time:String?){
        this.id= id
        this.title=title
        this.content=content
        this.time=time
    }
}
