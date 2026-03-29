**Client to DB Flow:**

`Client → Controller → Service → Repository → JPA → Hibernate → Hikari → JDBC → MySQL`

1. Request will go to Front Controller - Springboot implemented abstract controller will send request to appropriate controller. 
2. From there it will go to service - for business logic
3. Then repository t- get db items
4. Repository uses JPA - > Which used to map java objects to sql. 
5. Once it is mapped → Hibernate runs appropriate sql query. 
6. Hikari - Instead of creating DB connection every time → it keeps **pool of connections** → faster.
7. Then connects with db using JDBC driver. 

**What is the difference between @Controller and @RestController?**

- `@Controller` → returns HTML (View) / static pages
- `@RestController` → returns JSON (REST API)
- `@RestController = @Controller + @ResponseBody`
