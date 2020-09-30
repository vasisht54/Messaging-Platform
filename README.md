# Java Messaging App - CS5500 Summer I

### __Team name__: team-2-su20
### __Team members__: [@acfpho](https://github.ccs.neu.edu/acfpho), [@svasisht54](https://github.ccs.neu.edu/svasisht54), [@madgiamit](https://github.ccs.neu.edu/madgiamit) and [@apizzoccheri](https://github.ccs.neu.edu/apizzoccheri)
This repository contains the group project for *CS 5500 Foundation of Software Engineering* for the Summer I semester. Below is an overview of the team's workflow and branch management.

 
 ## Branching, commiting and merging
 There are two main branches that are constant, `master` and `develop`; `release` and `feature` branches are temporary branches that serve their purpose for a specific sprint/version or developmental feature.
 
1. `master` — this is your source of truth; code on this branch should be clean, well-documented, fully tested and bug-free. `develop` will be the _working_ version of this branch.
2. `release` — this is forked from `develop` towards the end of the sprint, once enough features have been completed. the branch will be tagged, and will be _merged_ into `master`. dependening, this branch can be merged back into `develop` if final changes/updates were made.
3. `develop` — this is the _working_ branch of `master`, where all `feature` branches are based off of. testing purposes only and to ensure individual code is cohesive and conflict-free. once you are done with your individual branches, submit a PR for review for merging. All merge conflicts are your responsibility and should be addressed here.
4. `feature` — these branches are forked from `develop` as it'll have the latest working code. You should run your own sonarqube tests on this branch and ensure quality before submitting a PR to `develop`.

## Git Workflow
 The branching and merging strategy referenced above is based off of: **[Gitflow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)**

 To create a new branch, checkout `develop` and run:
  ```
  git pull
  ```
 To get the most up to date version. Initiate all branch names with `issue-#` where `#` is the number of your issue, i.e. `issue-01-local-set-up`. The full command is:
 ```
 git checkout -b issue-#-some-descriptive-text
 ```
 Commits should be descriptive of the work that has been done, as a rule of thumb a commit would look like this:
 ```
 git commit -m "[issue-#] short description of the work"
 ```
 As you work through your issue and feel that you're done and ready to integrate:
 ```
 git push --set-upstream origin your_branch
 ```
Open a PR to `develop` and select at least 2 team members to request a code review. This is to ensure someone will review your code in a timely manner. You can open a PR either through the web GUI or commandline, _however_ it may be easier to just do this from the GUI because you can assign reviewers and labels, project, etc.

### For Approvers/Reviewers
- It is _recommended_ to `squash and merge` instead of the _default_ `create a merge commit`. This will keep the commit history clean/concise when pushing features into develop. Try to write a useful commit message for the merge commit so we know what the commit addresses and why.
    - [How to Squash and Merge in GH](https://help.github.com/en/github/administering-a-repository/configuring-commit-squashing-for-pull-requests)
    - [Example commit message](https://thoughtbot.com/blog/don-t-forget-the-silent-step-when-you-squash-and-merge), for a squash and merge.
    - [Explanation of the different Merge Types](https://rietta.com/blog/github-merge-types/)
- Delete the source branch once merged.

## Code Review
Code review is a crucial part of good development, as both the reviewer and reviewee, it is useful to keep in mind:

1. **Everyone's style is different** - when reviewing someone's code, ask yourself whether something is just a stylistic choice or an actual error.
2. **Don't be defensive** - code reviews make us better, if and when one your teammates points something out, don't take it as a personal assault on your skills.
3. **Not all feedback needs to be addressed** - Some comments may just be suggestions rather than actual requests, if you are confident in your choices, feel free to ignore it and move on.

## Commenting
When working with multiple developers, commeting helps others understand not only what you did, but *why* you did it. Every class, interface and method should have JavaDoc style comments to explain what the code is doing. Comment example:
```
/**
* Descriptive text of what the code does.
* @param example of a class or method parameter
* @return somethinf
* @throws SampleException, in case _something_ fails
*/
```

## Setting up MongoDB Atlas:
1.	Go to this [link](https://www.mongodb.com/cloud/atlas) and create your account.
2.	Once you have created your account, we need to create a cluster.
3.	Click on add new Cluster and select the free Cloud provider & region. (If the db goes down in that region then your data is gone) so select the free provider only if what you are going to store is not super important and work related.
4.	Once you have created your cluster you cannot rename it.
5.	After having created your cluster you need to create a db inside of it.
6.	To do so click on the cluster name > collections > create database.
7.	Enter your database name and collection name and click on create.
8.	Your database is ready.

## Connecting MongoDB Atlas with your Java Application:
1.	Copy the mongo driver [jar](https://mongodb.github.io/mongo-java-driver) into your project repository or add the following maven dependency in your pom file
```
		<dependency>
			<groupId>org.mongodb</groupId>
			<artifactId>mongo-java-driver</artifactId>
			<version>3.11.2</version>
		</dependency>
```
2.	Go to MongoDb Atlas, login with your credentials.
3.	Click on the cluster name > connect.
4.	Select Java Application and copy the URI generated. 
5.	Paste it in your java application to get the mongo client.
Creating Database user:
1.	Login to you MongoDB Atlas account.
2.	Click on Database Access under Security.
3.	Click on Add new database user and provide the username and password.
Reading the mongo connection URI:
mongodb+srv://```<dbuser>:<password>```@wbdv-sp20-yf48p.mongodb.net/```<dbname>```?retryWrites=true&w=majority
 
```<dbuser>``` is a placeholder for the database user you created.
```<password>``` is the database user password and not your Atlas password.
```<dbname>``` is the database name you want to connect to.

For additional information on Product Requirements, documentation, etc. check out the **[Project's Wikis](https://github.ccs.neu.edu/cs5500-fse/team-2-su20/wiki)**.
