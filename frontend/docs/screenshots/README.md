# Screenshots Directory

This directory contains screenshots for the Parche Lector frontend documentation.

## Required Screenshots

### Welcome View
- `welcome-view.png` - Main welcome screen with branding and CTAs
  
<img width="1934" height="1040" alt="welcome-view" src="https://github.com/user-attachments/assets/1136b4e8-e481-465c-a0c7-5363f8a25f3a" />

### Login View
- `login-view.png` - Login form empty state
  
  <img width="1923" height="951" alt="login-view" src="https://github.com/user-attachments/assets/6d2d7de9-54f4-4778-bafd-d2259c25ea20" />

- `login-errors.png` - Login form with validation errors
  
  <img width="1911" height="952" alt="login-errors" src="https://github.com/user-attachments/assets/f84d45e1-d4ba-4f0e-a84f-b724fbb2a9bf" />

- `login-loading.png` - Login form during submission
  
  <img width="1924" height="951" alt="login-loading" src="https://github.com/user-attachments/assets/d142ef3c-cae1-4abd-afae-cc7099d5bab1" />

### Register View
- `register-view.png` - Registration form empty state
  
  <img width="1918" height="950" alt="register-view" src="https://github.com/user-attachments/assets/79239549-8311-4c43-972e-a8ab3047021f" />

- `register-errors.png` - Registration form with validation errors
  
  <img width="1917" height="951" alt="register-errors" src="https://github.com/user-attachments/assets/2a2279de-5d17-41ef-82aa-30dc0a142fe3" />

### Home View
- `home-view.png` - Main dashboard with trending books
  
  <img width="1903" height="953" alt="home-view" src="https://github.com/user-attachments/assets/d477d3cf-3347-4003-a81e-8425402bdbcf" />

- `home-search.png` - Search results display
  
  <img width="1918" height="955" alt="home-search" src="https://github.com/user-attachments/assets/999649fa-a108-469a-97e9-2480c434b1bf" />

- `home-menu.png` - User menu dropdown expanded
  
  <img width="281" height="313" alt="home-menu" src="https://github.com/user-attachments/assets/a30d2fa7-9490-4383-8c0d-a643952a48e2" />

- `home-book-menu.png` - Book action menu (add to lists)
  
  <img width="237" height="452" alt="home-book-menu" src="https://github.com/user-attachments/assets/e2b6af72-1440-460b-afb1-796a679bccd7" />
  
- `home-mobile-nav.png` - Mobile bottom navigation
  
  <img width="488" height="631" alt="home-mobile-nav" src="https://github.com/user-attachments/assets/f3529c9b-572d-4d09-917d-2095081fd16b" />

- `home-book-card.png` - Individual book card detail
  
  <img width="160" height="402" alt="home-book-card" src="https://github.com/user-attachments/assets/f1422f1f-3303-4fb3-b9bb-614f96f9942f" />


### Profile View
- `profile-view.png` - Full profile page with user info
  
  <img width="974" height="719" alt="profile-view" src="https://github.com/user-attachments/assets/a15aa807-9b20-4450-8786-e58ccd227d13" />

- `profile-edit.png` - Edit profile modal opened
  
  <img width="1744" height="947" alt="profile-edit" src="https://github.com/user-attachments/assets/0cca3204-e212-47f1-8a21-d1dafc9e220d" />
  
- `profile-mobile.png` - Mobile profile layout
  
  <img width="369" height="810" alt="profile-mobile" src="https://github.com/user-attachments/assets/a53b0e7c-6478-4a55-97e6-334f104cc9f0" />


## Screenshot Guidelines

### Specifications
- **Format**: PNG (preferred) or WebP
- **Resolution**: Minimum 1280x720 for desktop views
- **Mobile Resolution**: 375x812 (iPhone standard)
- **Quality**: High quality, no compression artifacts
- **File Size**: Keep under 500KB when possible

### Capture Instructions

1. **Browser**: Use latest Chrome or Firefox
2. **Window Size**: 
   - Desktop: 1440px width minimum
   - Mobile: Use responsive mode at 375px width
3. **Clean State**: Remove any development tools or browser extensions from view
4. **Realistic Data**: Use realistic names, books, and content
5. **Consistent Theme**: Ensure all screenshots use same color scheme

### Content Guidelines

#### User Data
- Username: "Ana" or "Sebastian"
- Email: "ana@email.com"
- Avatar: Use DiceBear avatars with seed "Ana"

#### Book Data
- Use real books from Google Books API
- Ensure cover images are visible
- Show variety of ratings (3.5 - 5.0 stars)

#### States to Capture
1. **Empty States**: Show helpful empty messages
2. **Populated States**: Show realistic amounts of data (3-6 books per category)
3. **Error States**: Show user-friendly error messages
4. **Loading States**: Show spinners or skeletons

### Naming Convention

Follow the naming pattern:
```
<view>-<state>.png
```

Examples:
- `home-view.png` (default state)
- `home-search.png` (specific feature)
- `login-errors.png` (error state)

### How to Take Screenshots

#### Desktop Screenshots
1. Open the application in your browser
2. Set window width to 1440px
3. Navigate to the desired view/state
4. Use browser DevTools: `Cmd/Ctrl + Shift + P` → "Capture screenshot"
5. Or use OS screenshot tool (crop to content area)
6. Save to this directory

#### Mobile Screenshots
1. Open browser DevTools (F12)
2. Toggle device toolbar (Cmd/Ctrl + Shift + M)
3. Select "iPhone 12 Pro" or custom 375x812
4. Navigate to desired state
5. Capture screenshot
6. Save with `-mobile` suffix

### Image Optimization

After capturing screenshots:
1. Crop to relevant content area
2. Remove unnecessary whitespace
3. Optimize file size using tools like:
   - [TinyPNG](https://tinypng.com/)
   - [ImageOptim](https://imageoptim.com/)
   - `pngquant` command-line tool



