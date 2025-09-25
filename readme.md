# Password Manager

A secure JavaFX-based password management application that implements local encryption and storage of user credentials using Advanced Encryption Standard (AES) cryptographic protocols.

Group member:
Xiang Luo xxl1323
Shuai Fu sxf477
Yankai Xie yxx705

## Features

- **Cryptographic Security**: Implements AES encryption with Password-Based Key Derivation Function 2 (PBKDF2)
- **Master Password Authentication**: Single authentication credential protects all stored passwords
- **Local Data Storage**: All data is stored locally in encrypted format without external dependencies
- **Graphical User Interface**: JavaFX-based interface with dark theme implementation
- **Cross-Platform Compatibility**: Compatible with Windows, macOS, and Linux operating systems

## Installation and Execution

### System Requirements
- Java Development Kit (JDK) 21 or higher
- Maven wrapper included (no separate Maven installation required)

### Environment Configuration

Before running the application, ensure your JAVA_HOME environment variable is properly configured:

#### Windows Configuration
1. Verify Java installation location:
   ```powershell
   dir "C:\Program Files\Java"
   ```

2. Set JAVA_HOME to specific JDK installation:
   ```powershell
   # For current session
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   
   # For permanent configuration
   setx JAVA_HOME "C:\Program Files\Java\jdk-21" /M
   ```

3. Restart terminal after permanent configuration

#### macOS/Linux Configuration
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Application Execution
```bash
# Navigate to the PasswordManager directory
cd PasswordManager

# Execute the application
.\mvnw.cmd javafx:run    # Windows
./mvnw javafx:run        # macOS/Linux
```

### Troubleshooting Common Issues

#### Issue: "JAVA_HOME is set to an invalid directory"
**Solution**: Ensure JAVA_HOME points to a specific JDK installation directory, not the parent Java directory.

#### Issue: "mvnw.cmd is not recognized"
**Solution**: Verify you are in the correct PasswordManager subdirectory where the Maven wrapper files are located.

#### Issue: "Module javafx.controls not found"
**Solution**: Use the Maven wrapper instead of direct Java execution, as it handles JavaFX module path configuration automatically.

### Application Functionality
Upon successful execution, the application provides:
1. Master password creation and authentication
2. Password entry management (create, read, update, delete)
3. Automatic encryption and persistent storage

## Cryptographic Implementation

### Security Architecture
1. **Salt Generation**: Generates cryptographically secure random salt values for each master password
2. **Key Derivation**: Master password and salt undergo 100,000 PBKDF2 iterations to derive encryption keys
3. **Data Encryption**: User credentials are encrypted using AES-256 encryption standard
4. **Persistent Storage**: Only encrypted data is written to `passwords.txt` - plaintext credentials are never persisted

### Security Properties
- **Cryptographic Resistance**: Encrypted data remains secure without the master password authentication
- **Brute Force Mitigation**: High iteration count significantly increases computational cost of password attacks
- **Cryptographic Uniqueness**: Each installation generates unique encryption parameters independent of master password selection

## Project Structure
```
PasswordManager/
├── src/main/java/          # Java source code implementation
├── src/main/resources/     # FXML user interface definitions
├── passwords.txt          # Encrypted credential storage (generated automatically)
├── pom.xml               # Maven project configuration
└── mvnw.cmd             # Maven wrapper executable
```

## Privacy and Security Model
- **Offline Operation**: All data processing occurs locally without network connectivity
- **Local Data Persistence**: Credentials remain on the local filesystem exclusively
- **Open Source Transparency**: Source code available for security audit and verification
- **Industry Standard Cryptography**: Implements cryptographic standards used in financial and government applications

## Operational Guidelines
- Select a cryptographically strong master password with sufficient entropy
- The application generates a `passwords.txt` file in the project directory for data persistence
- Implement regular backup procedures for the `passwords.txt` file to prevent data loss
- Maintain master password confidentiality and avoid disclosure to unauthorized parties

## Technical Implementation
Developed using:
- **Java 21**: Latest long-term support Java platform features
- **JavaFX**: Cross-platform graphical user interface framework
- **Apache Maven**: Dependency management and build automation
- **AES-256 Encryption**: Advanced Encryption Standard with 256-bit key length
- **PBKDF2**: Password-Based Key Derivation Function 2 for secure key generation

---
*Secure credential management through local encryption and user-controlled access.*