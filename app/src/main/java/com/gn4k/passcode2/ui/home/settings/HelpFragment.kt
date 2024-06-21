package com.gn4k.passcode2.ui.home.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.adapter.FAQAdapter


class HelpFragment : Fragment() {

    lateinit var view1: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_help, container, false)

        val recyclerView: RecyclerView = view1.findViewById(R.id.recyclerview)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val adapter = FAQAdapter(faqList)
        recyclerView.adapter = adapter

        return view1;
    }

    companion object {
        val faqList = listOf(
            Pair(
                "What is a password manager and why do I need one?",
                "A password manager is a secure tool that stores and manages your passwords for various online accounts. It helps you generate strong, unique passwords and keeps them encrypted, so you only need to remember one master password to access them all. Using a password manager enhances your online security by reducing the risk of password-related vulnerabilities like using weak or repeated passwords."
            ),
            Pair(
                "How does your password manager work to keep my passwords secure?",
                "Our password manager employs industry-standard encryption protocols to safeguard your passwords and personal data. All data stored in the app is encrypted both in transit and at rest, ensuring that even if intercepted, it remains unreadable to unauthorized parties."
            ),
            Pair(
                "What platforms does your password manager support?",
                "Our password manager is available on a wide range of platforms including iOS, Android, Windows, macOS, and web browsers like Chrome, Firefox, and Safari, ensuring seamless access across all your devices."
            ),
            Pair(
                "Is my data encrypted?",
                "Yes, all your data, including passwords, notes, and other sensitive information, is encrypted using advanced encryption algorithms. This encryption ensures that even if someone gains unauthorized access to your device or our servers, they won't be able to decipher your data without your master password."
            ),
            Pair(
                "Can I access my passwords across multiple devices?",
                "Yes, with our password manager, you can sync your data across all your devices securely. This means you can access your passwords from your smartphone, tablet, laptop, or any other device with our app installed."
            ),
            Pair(
                "How do I import my existing passwords into the app?",
                "Our password manager provides easy-to-use tools for importing passwords from various sources such as web browsers or other password managers. Simply follow the instructions within the app to import your existing passwords effortlessly."
            ),
            Pair(
                "What happens if I forget my master password?",
                "If you forget your master password, unfortunately, there's no way to recover it since we don't have access to your master password or your encrypted data. However, you can reset your account and start fresh, but you'll lose access to your stored passwords. This is why it's crucial to remember your master password or use a secure method to store it."
            ),
            Pair(
                "How can I generate strong and secure passwords with your app?",
                "Our password manager includes a password generator feature that allows you to generate strong and unique passwords with customizable settings such as length, character types, and exclusion of similar characters to prevent confusion."
            ),
            Pair(
                "Are there any limitations on the number of passwords I can store?",
                "No, there are no limitations on the number of passwords or items you can store in our password manager. You can securely store as many passwords, credit card details, secure notes, or other sensitive information as you need."
            ),
            Pair(
                "Is there a way to share passwords securely with family or colleagues?",
                "Yes, our password manager offers a secure password sharing feature that allows you to share specific passwords or entire vaults with trusted individuals. You can control permissions and revoke access at any time."
            )
        )
    }
}