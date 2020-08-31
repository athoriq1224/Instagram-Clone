package com.example.instagram.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapter.UserAdapter
import com.example.instagram.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: UserAdapter? = null
    private var myUser: MutableList<User>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.search_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        myUser = ArrayList()
        userAdapter = context?.let { UserAdapter(it,myUser as ArrayList<User>,true) }
        recyclerView?.adapter = userAdapter

        view.search_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (view.search_editText.toString()==""){
                }else{

                    recyclerView?.visibility = View.VISIBLE
                    getUsers()

                    searchUser(s.toString().toString().toLowerCase())
                }
            }
        })
        return view
    }

    private fun searchUser(input: String) {
        //untuk get data dri database
        val query = FirebaseDatabase.getInstance().getReference()
            .child("User")
            .orderByChild("fullname")
            .startAt(input).endAt(input+ "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                myUser?.clear()

                for (snapshot in p0.children){
                    val user = snapshot.getValue(User::class.java)
                    if (user !=null){
                        myUser?.add(user)
                    }
                }
                userAdapter?.notifyDataSetChanged()
            }
        })

    }

    private fun getUsers() {
        val userRef = FirebaseDatabase.getInstance().getReference().child("User")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (view?.search_editText?.toString()==""){

                    for (snapshot in p0.children){
                        val user = snapshot.getValue(User::class.java)
                        if (user!=null){
                            myUser?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }

            }
        })
    }


}