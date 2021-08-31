# How to Run

This needs ```Docker``` to run. When you install docker, on mac, you can use the following command:

    docker run --name ejabberd -p 5222:5222 ejabberd/ecs

With this, you can register new users, using letters, as that match the regex ```[a-i]```. To do so, use the following
command:

    docker exec -it ejabberd bin/ejabberdctl register i localhost password

After this, you can go to the project and run

    npm install

and

    node index.js <VERSION>

where VERSION is any letter from A to I. Run many servers at the same time for the program to work correctly.
