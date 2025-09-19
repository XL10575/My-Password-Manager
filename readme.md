# 🔐 Password Manager

A secure JavaFX-based password manager that encrypts and stores your passwords locally using military-grade AES encryption.

## ✨ Features

- **Secure Encryption**: Uses AES encryption with PBKDF2 key derivation
- **Master Password Protection**: Single master password protects all your passwords
- **Local Storage**: All data stored locally in encrypted format
- **User-Friendly GUI**: Clean JavaFX interface with dark theme
- **Cross-Platform**: Works on Windows, Mac, and Linux

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher installed
- No additional setup required (Maven wrapper included)

### Running the Application
```bash
# Navigate to the PasswordManager directory
cd PasswordManager

# Start the application
.\mvnw.cmd javafx:run    # Windows
./mvnw javafx:run        # Mac/Linux
```

The application will open with a GUI where you can:
1. Create a master password (first time)
2. Add, edit, and delete passwords
3. All passwords are automatically encrypted and saved

## 🔒 How Security Works

### Simple Explanation
1. **Salt Generation**: Creates a unique random "seasoning" for your master password
2. **Key Creation**: Your master password + salt is processed 100,000 times to create an encryption key
3. **Password Encryption**: Each password is scrambled using military-grade AES encryption
4. **File Storage**: Only encrypted data is saved to `passwords.txt` - your actual passwords are never stored in plain text

### Security Benefits
- **Uncrackable**: Even if someone steals your password file, they cannot decrypt it without your master password
- **Brute Force Protection**: 100,000 iterations make password guessing extremely slow
- **Unique Encryption**: Each password file uses different encryption, even with the same master password

## 📁 File Structure
```
PasswordManager/
├── src/main/java/          # Java source code
├── src/main/resources/     # FXML UI files
├── passwords.txt          # Encrypted password storage (created automatically)
├── pom.xml               # Maven configuration
└── mvnw.cmd             # Maven wrapper (no Maven installation needed)
```

## 🛡️ Privacy & Security
- **No Internet Connection**: All data stays on your computer
- **No Cloud Storage**: Passwords never leave your device
- **Open Source**: You can inspect the code to verify security
- **Industry Standard**: Uses the same encryption methods as banks and governments

## 💡 Usage Tips
- Choose a strong master password you can remember
- The application creates a `passwords.txt` file in the project directory
- Back up your `passwords.txt` file to prevent data loss
- Never share your master password with anyone

## 🔧 Development
Built with:
- **Java 21**: Modern Java features
- **JavaFX**: Cross-platform GUI framework
- **Maven**: Dependency management and build tool
- **AES Encryption**: 256-bit encryption standard
- **PBKDF2**: Password-based key derivation function

---
*Your passwords, your control. Secure, simple, and private.*