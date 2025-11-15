@echo off
SETLOCAL

REM Ruta de ADB
SET ADB_PATH="C:\Users\ziclo\AppData\Local\Android\Sdk\platform-tools\adb.exe"

REM Nombre del paquete
SET PACKAGE=com.pph.demoapp

REM Mostrar mensaje de inicio
echo Cerrando procesos activos...

taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM gradle.exe >nul 2>&1
taskkill /F /IM studio64.exe >nul 2>&1
taskkill /F /IM kotlinc.exe >nul 2>&1

echo Borrando caches...
rmdir /S /Q "%USERPROFILE%\.gradle\caches"
rmdir /S /Q "%USERPROFILE%\.android\build-cache"

echo Desinstalando APK anterior (si existe)...
%ADB_PATH% uninstall %PACKAGE%

echo Compilando proyecto desde cero...
gradlew.bat clean
gradlew.bat :app:assembleDebug --no-build-cache

echo Instalando nuevo APK...
%ADB_PATH% install app\build\outputs\apk\debug\app-debug.apk

echo ------------------------------
echo ✅ Todo listo: APK limpio instalado
echo ------------------------------
pause
ENDLOCAL