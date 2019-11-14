package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;


@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public EmployeesIndexServlet() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //ページ数を得る
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e){ }

        //ページの表示件数を得る
        List<Employee> employees = em.createNamedQuery("getAllEmployees",  Employee.class)
                                    .setFirstResult(15 * (page -1))
                                    .setMaxResults(15)
                                    .getResultList();

        //データベースに登録されている全件数を得る
        long employees_count = em.createNamedQuery("getEmployeesCount", Long.class)
                                .getSingleResult();

        em.close();

        //各値をリクエストスコープに格納
        request.setAttribute("page", page);
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);

        //セッションスコープにフラッシュメッセージが格納されている場合は、リクエストスコープに格納する
        //不要となったセッションスコープに格納されたデータを削除
        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request,response);
    }

}
