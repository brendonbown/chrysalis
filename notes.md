# Notes
* When [`kotlinx-cli`](https://github.com/Kotlin/kotlinx-cli) becomes stable, use it
* Add support for multiple products
* Add support for a 'repl' style command
* Write a README
* Add option to specify limitation_type/limitation_value
* Add option to get permissions based on speed url (**In progress/on hold**)
  * ```sql
      select informational_area
      from web_resource
      join web_resource_area using(web_resource_id)
      where speed_url = 'ADV11'
    ```
* Refactor to make it more clear how things work (**In progress**)