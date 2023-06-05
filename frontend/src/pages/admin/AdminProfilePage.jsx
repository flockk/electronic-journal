import Navbar from "../../components/layout/Navbar";
import React from "react";
import {Link} from "react-router-dom";
import Card from "../../components/cards/Card";
import Footer from "../../components/layout/Footer";

const AdminProfilePage = () => {

    const navigation = [
        {
            name: <Link to="/admin/users">Управление пользователями</Link>,
            current: false
        },
    ];

    const footerElements = [
        <Link to="/admin/users">Управление пользователями</Link>,
    ];

    return (
        <div className="flex flex-col min-h-screen mt-16">
            <Navbar navigation={navigation} profileLink="/admin/profile"/>
            <Card title="Ваш  профиль"
                  description="Вы можете изменить настройки своего профиля"
                  saveButtonText="Сохранить"
            />
            <Footer elements={footerElements}/>
        </div>
    );
}

export default AdminProfilePage;
