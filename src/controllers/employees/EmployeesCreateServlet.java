package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmployeesCreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            Employee e = new Employee();

            //フォームから受け取った値をEmployeeクラスにセットする
            String code = request.getParameter("code");
            e.setCode(code);
            String name = request.getParameter("name");
            e.setName(name);
            String password = request.getParameter("password");
            e.setPassword(EncryptUtil.getPasswordEncrypt(password,(String)this.getServletContext().getAttribute("salt")));
            Integer admin_flag = Integer.parseInt(request.getParameter("admin_flag"));
            e.setAdmin_flag(admin_flag);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            e.setDelete_flag(0);


            //バリデーションを実行
            List<String> errors = EmployeeValidator.validate(e, true, true);
            if(errors.size() > 0){
                em.close();

                request.setAttribute("errors", errors);
                request.setAttribute("employee", e);
                request.setAttribute("_token", request.getSession().getId());

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
                rd.forward(request, response);
            }else{
                em.getTransaction().begin();
                em.persist(e);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
