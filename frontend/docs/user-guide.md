# User Guide - Parche Lector

Welcome to **Parche Lector**, your community for discovering and tracking books! This guide will help you get started with the application.

---

## Table of Contents

1. [Getting Started](#getting-started)
2. [Creating an Account](#creating-an-account)
3. [Logging In](#logging-in)
4. [Discovering Books](#discovering-books)
5. [Managing Your Reading Lists](#managing-your-reading-lists)
6. [Editing Your Profile](#editing-your-profile)
7. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Accessing the Application

1. Open your web browser
2. Navigate to the Parche Lector URL
3. You'll see the welcome screen

![Welcome Screen](./screenshots/welcome-view.png)

### First Steps

From the welcome screen, you can:
- **Iniciar sesión** - Log in if you already have an account
- **Crear cuenta** - Create a new account

---

## Creating an Account

### Registration Process

1. Click the **"Crear cuenta"** button from the welcome screen
2. Fill in the registration form:
   - **Username**: Choose a unique username (letters, numbers, dots, and underscores)
   - **Email**: Enter a valid email address
   - **Password**: Create a password (minimum 6 characters)

![Registration Form](./screenshots/register-view.png)

3. Click **"Crear cuenta"** to submit
4. If successful, you'll be automatically logged in and redirected to the home page

### Registration Tips

✅ **Valid usernames**: `ana.reader`, `book_lover123`, `sebastian.castaneda`  
❌ **Invalid usernames**: `user@name`, `user name`, `user#123`

### Common Registration Errors

![Registration Validation Errors](./screenshots/register-errors.png)

- **"Este campo es requerido"**: You must fill in all fields
- **"Email inválido"**: Enter a valid email format (e.g., user@example.com)
- **"La contraseña debe tener al menos 6 caracteres"**: Your password is too short

---

## Logging In

### Login Process

1. Click **"Iniciar sesión"** from the welcome screen
2. Enter your credentials:
   - **Username or Email**: You can use either
   - **Password**: Your account password

![Login Form](./screenshots/login-view.png)

3. Click **"Iniciar sesión"**
4. Upon success, you'll be redirected to your home dashboard

### Forgot Password?

Click the **"¿Olvidaste tu contraseña?"** link to recover your account.

![Login Errors](./screenshots/login-errors.png)

### Login Troubleshooting

- Ensure your username/email is correct
- Check that Caps Lock is off
- Verify you're using the correct password

---

## Discovering Books

### Home Dashboard

Once logged in, you'll see your personalized home dashboard.

![Home Dashboard](./screenshots/home-view.png)

### Features

1. **Personalized Greeting**: See your name in the welcome message
2. **Trending Books**: Discover popular books in the community
3. **Search Functionality**: Find specific books
4. **Book Actions**: Add books to your reading lists

### Searching for Books

1. Locate the search bar at the top of the books section
2. Type your search query (book title, author, etc.)
3. Click the search icon or press Enter
4. Results will appear below

![Search Results](./screenshots/home-search.png)

### Book Cards

Each book card displays:
- **Cover Image**: Visual representation of the book
- **Title**: Book name
- **Author**: Book author(s)
- **Rating**: Star rating (out of 5)
- **Add Button (+)**: Add to reading lists

![Book Card Detail](./screenshots/home-book-card.png)

### Adding Books to Your Lists

1. Click the **+** button on any book card
2. A menu will appear with three options:
   - **📖 Leyendo** (Currently Reading)
   - **✅ Leído** (Finished)
   - **📚 Por leer** (To Read)

![Book Action Menu](./screenshots/home-book-menu.png)

3. Click your desired status
4. The book will be added to your profile

---

## Managing Your Reading Lists

### Accessing Your Profile

There are two ways to access your profile:

**Option 1: User Menu**
1. Click your avatar in the top-right corner
2. Click **"Mi perfil"** from the dropdown menu

![User Menu](./screenshots/home-menu.png)

**Option 2: Bottom Navigation (Mobile)**
1. Tap the **"Perfil"** icon in the bottom navigation

![Mobile Bottom Navigation](./screenshots/home-mobile-nav.png)

### Profile Overview

Your profile displays:
- **Profile Information**: Avatar, name, and bio
- **Reading Statistics**: Total books in each category
- **Reading Lists**: Organized by status

![Profile Page](./screenshots/profile-view.png)

### Reading List Categories

#### 📖 Leyendo (Currently Reading)
Books you're actively reading right now.

#### ✅ Leídos (Finished)
Books you've completed.

#### 📚 Por leer (To Read)
Books you want to read in the future.

![Reading Lists Detail](./screenshots/profile-lists.png)

### Empty Lists

If a category has no books, you'll see:
> "No hay libros en esta categoría."

![Empty List State](./screenshots/profile-empty.png)

---

## Editing Your Profile

### Opening the Editor

1. Go to your profile page
2. Click the **"Editar perfil"** button

![Edit Profile Button](./screenshots/profile-view.png)

### Edit Profile Modal

The modal allows you to edit:
- **Name**: Your display name
- **Bio**: A short description about yourself
- **Avatar URL**: Link to your profile picture

![Edit Profile Modal](./screenshots/profile-edit.png)

### Saving Changes

1. Make your desired changes
2. Click **"Guardar"** to save
3. Click **"Cancelar"** to discard changes

### Avatar Tips

You can use:
- Direct image URLs (e.g., `https://example.com/avatar.jpg`)
- Avatar generators like [DiceBear](https://avatars.dicebear.com/)

Example avatar URL:
```
https://api.dicebear.com/7.x/avataaars/svg?seed=YourName&backgroundColor=F9C846
```

---

## User Menu Options

Click your avatar to access:

![User Menu Expanded](./screenshots/home-menu.png)

- **🏠 Inicio**: Return to home dashboard
- **👤 Mi perfil**: View your profile
- **🚪 Cerrar sesión**: Log out of your account

---

## Troubleshooting

### I can't log in
- Verify your username/email and password are correct
- Ensure you've created an account
- Try using your email instead of username (or vice versa)

### Books aren't loading
- Check your internet connection
- Try refreshing the page
- Click the **"Reintentar"** button if you see an error

![Loading Error State](./screenshots/home-error.png)

### My profile changes aren't saving
- Ensure all fields are filled correctly
- Check your internet connection
- Try logging out and back in

### Search isn't working
- Make sure you've entered a search query
- Try different search terms
- Check for spelling errors

### Images aren't displaying
- Check your internet connection
- The Google Books API may not have cover images for all books
- Try refreshing the page

---

## Mobile Experience

The application is fully responsive and optimized for mobile devices.

### Mobile Features
- Touch-friendly interface
- Bottom navigation bar for easy access
- Optimized layouts for smaller screens
- Swipe gestures for menus

![Mobile Welcome View](./screenshots/welcome-mobile.png)

![Mobile Profile View](./screenshots/profile-mobile.png)

---

## Tips for Best Experience

### ✨ Pro Tips

1. **Use Search**: Find specific books quickly instead of scrolling
2. **Keep Lists Organized**: Move books between lists as your reading progresses
3. **Update Your Profile**: Add a bio to personalize your experience
4. **Check Trending**: Discover new books from community recommendations

### 🎯 Reading List Strategy

- **Leyendo**: Limit to 2-3 books at a time to stay focused
- **Leídos**: Keep track of your accomplishments
- **Por leer**: Build your reading queue for future

---

## Getting Help

If you encounter issues not covered in this guide:

1. **Refresh the page**: Many issues resolve with a simple refresh
2. **Check your connection**: Ensure you have stable internet
3. **Try another browser**: Sometimes browser-specific issues occur
4. **Clear cache**: Old cached data can cause problems

---

## System Requirements

### Supported Browsers
- ✅ Google Chrome (latest)
- ✅ Mozilla Firefox (latest)
- ✅ Safari (latest)
- ✅ Microsoft Edge (latest)

### Recommended
- Modern browser with JavaScript enabled
- Stable internet connection for optimal experience
- Minimum screen resolution: 360px width

---

## Privacy & Security

### Your Data
- Passwords are securely stored
- Sessions expire after inactivity
- Always log out on shared computers

### Account Security
- Use a strong, unique password
- Don't share your credentials
- Log out when finished

---

**Happy Reading! 📚**

---

**User Guide Version**: 1.0  
**Last Updated**: 2025-11-21
