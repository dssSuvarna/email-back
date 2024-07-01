package com.emailmarketingtool.services

import com.emailmarketingtool.entities.Contact
import com.emailmarketingtool.entities.Sender
import jakarta.activation.DataHandler
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import jakarta.mail.util.ByteArrayDataSource
import org.json.JSONObject
import org.jsoup.Jsoup
import org.springframework.mail.MailException
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@Service
class EmailService {

    fun sendEmail(campaignId: Long, emailRequest: EmailRequest, sender: Sender) {
//        val ngrokUrl = fetchNgrokUrl()
        try {
            val props = Properties()
            props["mail.transport.protocol"] = "smtp"
            props["mail.smtp.auth"] = "true"
            props["mail.debug"] = "true"
            props["mail.smtp.host"] = sender.host

            if (sender.port == 587) {
                props["mail.smtp.port"] = sender.port
                props["mail.smtp.starttls.enable"] = "true"
            }
            if (sender.port == 465) {
                props["mail.smtp.port"] = sender.port
                props["mail.smtp.ssl.enable"] = "true"
            }
            if (sender.port != 465 && sender.port != 587) {
                props["mail.smtp.port"] = 465
                props["mail.smtp.ssl.enable"] = "true"
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(sender.mail, sender.passKey)
                }
            })
            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(sender.mail))
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRequest.contact.email))
            msg.subject = emailRequest.subject

            // Create a MimeMultipart to hold both text and image parts
            val multipart = MimeMultipart()

            // Create the text part
            val modifiedBody = emailRequest.body
                .replace("\${name}", emailRequest.contact.name?: "")
                .replace("\${company}", emailRequest.contact.company?: "")

            // Remove the Base64 image data from the HTML body
            val cleanedBody = removeBase64Images(modifiedBody)

            // Append the tracking pixel to the cleaned body
            val trackingPixel = """<a href="https://email-markting-front.vercel.app/email-status">Click here</a>"""
            val bodyWithTrackingPixel = "$cleanedBody $trackingPixel"

            // Create the text part with the cleaned body
            val textPart = MimeBodyPart()
            textPart.setContent(bodyWithTrackingPixel, "text/html")
            multipart.addBodyPart(textPart)

            // Extract Base64 images from the body and add them as attachments
            val base64Images = extractBase64Images(modifiedBody)
            base64Images.forEach { base64Image ->
                val imagePart = MimeBodyPart()
                val dataSource = ByteArrayDataSource(Base64.getDecoder().decode(base64Image), "image/png")
                imagePart.dataHandler = DataHandler(dataSource)
                imagePart.setHeader("Content-ID", "<image>")
                imagePart.setHeader("Content-Type", "image/png; name=image.png")
                imagePart.setHeader("Content-Transfer-Encoding", "base64")
                imagePart.disposition = MimeBodyPart.INLINE
                multipart.addBodyPart(imagePart)
            }

            // Set the multipart content to the email message
            msg.setContent(multipart)

            println("Message is ready")
            Transport.send(msg)

            println("Email Sent Successfully!!")

        } catch (e: MailException) {
            println("Failed to send email: ${e.message}")
        } catch (e: Exception) {
            println("Failed to send email: ${e.message}")
        }
    }

}

fun extractBase64Images(htmlBody: String): List<String> {
    val regex = Regex("""<img[^>]*src="data:image/[^;]+;base64,([^"]+)"[^>]*>""")
    val matches = regex.findAll(htmlBody)
    return matches.map { it.groupValues[1] }.toList()
}

fun removeBase64Images(htmlBody: String): String {
    val document = Jsoup.parse(htmlBody)  // Parse HTML with Jsoup
    val images = document.select("img[src^=data:image]")  // Select images with data:image source

    for (image in images) {
        image.remove() // Directly remove the image element
    }
    return document.outerHtml()  // Return the modified HTML
}

//fun fetchNgrokUrl(): String {
//    val url = URL("http://ngrok:4040/api/tunnels")
//    with(url.openConnection() as HttpURLConnection) {
//        requestMethod = "GET"
//        inputStream.bufferedReader().use {
//            val response = it.readText()
//            val jsonResponse = JSONObject(response)
//            val tunnels = jsonResponse.getJSONArray("tunnels")
//            if (tunnels.length() > 0) {
//                val firstTunnel = tunnels.getJSONObject(0)
//                return firstTunnel.getString("public_url")
//            }
//        }
//    }
//    throw RuntimeException("Ngrok tunnel not found")
//}


data class EmailRequest(
    val subject: String,
    val body: String,
    val contact: Contact
)