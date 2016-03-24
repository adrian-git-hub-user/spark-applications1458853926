#Creates a github repo with parent folder and timestamp as name, all current files/folders are added to github repo
#This should be run using git bash for windows : https://git-for-windows.github.io/
#To run navigate to dir that contains this script (script should be added to root dir of project) and run with command "name_of_script.sh"
#May need to set ssh keys, see https://help.github.com/articles/generating-a-new-ssh-key/ , https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/

foldername="${PWD##*/}"
timestamp=$(date +%s)
projectname=$foldername$timestamp

echo $projectname

curl -i https://api.github.com/user/repos -u adrian-git-hub-user:$githubaccess -d '{"name":"'"$projectname"'"}'

git init
git add .
git commit -m "first commit"
{ # your 'try' block
	git remote rm origin
} 

git remote add origin "git@github.com:adrian-git-hub-user/$projectname.git"
git push -u origin master