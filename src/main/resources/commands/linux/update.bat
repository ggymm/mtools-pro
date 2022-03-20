call cd D:\Code\github\linux-command
call D:
call git pull
call rmdir /S /Q html 2> NUL
call mkdir html 2> NUL
call cd command
for %%i in (*) do (
    echo %%i
    call pandoc %%i -o ..\html\%%i.html -s  --template=..\..\pandoc-html.template --metadata title="%%i.html"
)
call cd ..
call xcopy html %~dp0html /S /F /Y
@pause